package io.fluent.qabox.service;

import com.google.gson.JsonObject;
import io.fluent.qabox.constant.JavaType;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.*;
import io.fluent.qabox.frontend.fun.VLModel;
import io.fluent.qabox.processor.DataProcessorManager;
import io.fluent.qabox.query.BoxQuery;
import io.fluent.qabox.query.ColumnQuery;
import io.fluent.qabox.util.AnnotationUtil;
import io.fluent.qabox.util.BoxUtil;
import io.fluent.qabox.util.ExcelUtil;
import io.fluent.qabox.util.misc.DateUtil;
import io.fluent.qabox.view.BoxFieldModel;
import io.fluent.qabox.view.BoxModel;
import io.fluent.qabox.view.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class BoxExcelService {

  public static final String XLS_FORMAT = ".xls";

  public static final String XLSX_FORMAT = ".xlsx";

  private static final String SIMPLE_CELL_ERR = "请选择或输入有效的选项，或下载最新模版重试！";

  /**
   * excel导出，展示的格式和view表格一致
   *
   * @return Workbook
   */
  public Workbook exportExcel(BoxModel boxModel, Page page) {
    // XSSFWorkbook、SXSSFWorkbook
    Workbook wb = new SXSSFWorkbook();
    Sheet sheet = wb.createSheet(boxModel.getBox().name());
    sheet.setZoom(160);
    //冻结首行
    sheet.createFreezePane(0, 1, 1, 1);
    int rowIndex = 0;
    int colNum = 0;
    Row row = sheet.createRow(rowIndex);
    CellStyle headStyle = ExcelUtil.beautifyExcelStyle(wb);
    Font headFont = wb.createFont();
    headFont.setColor(IndexedColors.WHITE.index);
    headStyle.setFont(headFont);
    headStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
    headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    headFont.setBold(true);
    headStyle.setFont(headFont);
    int cellNum = 0;
    for (BoxFieldModel fieldModel : boxModel.getEruptFieldModels()) {
      for (View view : fieldModel.getUiField().views()) {
        cellNum++;
        if (view.show() && view.export()) {
          sheet.setColumnWidth(cellNum, (view.title().length() + 10) * 256);
          Cell cell = row.createCell(colNum);
          cell.setCellStyle(headStyle);
          cell.setCellValue(view.title());
          colNum++;
        }
      }
    }
    CellStyle style = ExcelUtil.beautifyExcelStyle(wb);
    for (Map<String, Object> map : page.getList()) {
      int dataColNum = 0;
      row = sheet.createRow(++rowIndex);
      for (BoxFieldModel fieldModel : boxModel.getEruptFieldModels()) {
        for (View view : fieldModel.getUiField().views()) {
          if (view.show() && view.export()) {
            Cell cell = row.createCell(dataColNum);
            cell.setCellStyle(style);
            Object val;
            if (StringUtils.isNotBlank(view.column())) {
              val = map.get(fieldModel.getFieldName() + "_" + view.column().replace(".", "_"));
            } else {
              val = map.get(fieldModel.getFieldName());
            }
            Edit edit = fieldModel.getUiField().edit();
            Optional.ofNullable(val).ifPresent(it -> {
              String str = it.toString();
              if (edit.type() == EditType.BOOLEAN || view.type() == ViewType.BOOLEAN) {
                if (edit.boolType().trueText().equals(str)) {
                  cell.setCellValue(edit.boolType().trueText());
                } else if (edit.boolType().falseText().equals(str)) {
                  cell.setCellValue(edit.boolType().falseText());
                }
              } else {
                cell.setCellValue(str);
              }
            });
            dataColNum++;
          }
        }
      }
    }
    return wb;
  }

  public List<JsonObject> excelToEruptObject(BoxModel boxModel, Workbook workbook) throws Exception {
    Sheet sheet = workbook.getSheetAt(0);
    Row titleRow = sheet.getRow(0);
    Map<String, BoxFieldModel> editTitleMappingEruptField = new HashMap<>(boxModel.getEruptFieldModels().size());
    for (BoxFieldModel fieldModel : boxModel.getEruptFieldModels()) {
      editTitleMappingEruptField.put(fieldModel.getUiField().edit().title(), fieldModel);
    }
    Map<Integer, BoxFieldModel> cellIndexMapping = new HashMap<>(titleRow.getPhysicalNumberOfCells());
    Map<Integer, Map<String, Object>> cellIndexJoinEruptMap = new HashMap<>();
    for (int i = 0; i < titleRow.getPhysicalNumberOfCells(); i++) {
      String titleName = titleRow.getCell(i).getStringCellValue();
      BoxFieldModel BoxFieldModel = editTitleMappingEruptField.get(titleName);
      cellIndexMapping.put(i, BoxFieldModel);
      Edit edit = BoxFieldModel.getUiField().edit();
      switch (edit.type()) {
        case CHOICE:
          Map<String, String> map = BoxUtil.getChoiceMap(edit.choiceType());
          Map<String, Object> choiceMap = new HashMap<>(map.size());
          for (Map.Entry<String, String> entry : map.entrySet()) {
            choiceMap.put(entry.getValue(), entry.getKey());
          }
          cellIndexJoinEruptMap.put(i, choiceMap);
          break;
        case BOOLEAN:
          Map<String, Object> boolMap = new HashMap<>(2);
          BoolType boolType = BoxFieldModel.getUiField().edit().boolType();
          boolMap.put(boolType.trueText(), true);
          boolMap.put(boolType.falseText(), false);
          cellIndexJoinEruptMap.put(i, boolMap);
          break;
        case REFERENCE_TREE:
          IBoxDataService IBoxDataService = DataProcessorManager.getDataProcessor(boxModel.getClazz());
          List<ColumnQuery> columns = new ArrayList<>();
          columns.add(new ColumnQuery(edit.referenceTreeType().id(), edit.referenceTreeType().id()));
          columns.add(new ColumnQuery(edit.referenceTreeType().label(), edit.referenceTreeType().label()));
          Collection<Map<String, Object>> list = IBoxDataService.queryColumn(BoxCoreService.getBoxModel(BoxFieldModel.getFieldReturnName()), columns, BoxQuery.builder().build());
          Map<String, Object> refTreeMap = new HashMap<>(list.size());
          for (Map<String, Object> m : list) {
            Object label = m.get(edit.referenceTreeType().label());
            if (null == label) {
              continue;
            }
            refTreeMap.put(label.toString(), m.get(edit.referenceTreeType().id()));
          }
          cellIndexJoinEruptMap.put(i, refTreeMap);
          break;
        case REFERENCE_TABLE:
          IBoxDataService eruptDataProcessor = DataProcessorManager.getDataProcessor(boxModel.getClazz());
          List<ColumnQuery> columnList = new ArrayList<>();
          columnList.add(new ColumnQuery(edit.referenceTableType().id(), edit.referenceTableType().id()));
          columnList.add(new ColumnQuery(edit.referenceTableType().label(), edit.referenceTableType().label()));
          Collection<Map<String, Object>> list2 = eruptDataProcessor.queryColumn(BoxCoreService.getBoxModel(BoxFieldModel.getFieldReturnName()), columnList, BoxQuery.builder().build());
          Map<String, Object> refTreeMap2 = new HashMap<>(list2.size());
          for (Map<String, Object> m : list2) {
            Object label = m.get(edit.referenceTableType().label());
            if (null == label) {
              continue;
            }
            refTreeMap2.put(label.toString(), m.get(edit.referenceTableType().id()));
          }
          cellIndexJoinEruptMap.put(i, refTreeMap2);
          break;
        default:
          break;
      }
    }
    List<JsonObject> listObject = new ArrayList<>();
    for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
      Row row = sheet.getRow(rowNum);
      if (row.getPhysicalNumberOfCells() == 0) {
        continue;
      }
      JsonObject jsonObject = new JsonObject();
      for (int cellNum = 0; cellNum < titleRow.getPhysicalNumberOfCells(); cellNum++) {
        Cell cell = row.getCell(cellNum);
        BoxFieldModel BoxFieldModel = cellIndexMapping.get(cellNum);
        if (null != cell && CellType.BLANK != cell.getCellType()) {
          Edit edit = BoxFieldModel.getUiField().edit();
          switch (edit.type()) {
            case REFERENCE_TABLE:
            case REFERENCE_TREE:
              JsonObject jo = new JsonObject();
              try {
                if (edit.type() == EditType.REFERENCE_TREE) {
                  jo.addProperty(edit.referenceTreeType().id(),
                    cellIndexJoinEruptMap.get(cellNum).get(cell.getStringCellValue()).toString());
                } else if (edit.type() == EditType.REFERENCE_TABLE) {
                  jo.addProperty(edit.referenceTableType().id(),
                    cellIndexJoinEruptMap.get(cellNum).get(cell.getStringCellValue()).toString());
                }
              } catch (Exception e) {
                throw new Exception(edit.title() + " -> " + this.getStringCellValue(cell) + "数据不存在");
              }
              jsonObject.add(BoxFieldModel.getFieldName(), jo);
              break;
            case CHOICE:
              try {
                jsonObject.addProperty(BoxFieldModel.getFieldName(), cellIndexJoinEruptMap.get(cellNum)
                  .get(cell.getStringCellValue()).toString());
              } catch (Exception e) {
                throw new Exception(edit.title() + " -> " + this.getStringCellValue(cell) + "数据不存在");
              }
              break;
            case BOOLEAN:
              Boolean bool = (Boolean) cellIndexJoinEruptMap.get(cellNum).get(cell.getStringCellValue());
              jsonObject.addProperty(BoxFieldModel.getFieldName(), bool);
              break;
            default:
              String rn = BoxFieldModel.getFieldReturnName();
              if (String.class.getSimpleName().equals(rn)) {
                jsonObject.addProperty(BoxFieldModel.getFieldName(), this.getStringCellValue(cell));
              } else if (JavaType.NUMBER.equals(rn)) {
                jsonObject.addProperty(BoxFieldModel.getFieldName(), cell.getNumericCellValue());
              } else if (BoxUtil.isDateField(BoxFieldModel.getFieldReturnName())) {
                jsonObject.addProperty(BoxFieldModel.getFieldName(), DateUtil.getSimpleFormatDateTime(cell.getDateCellValue()));
              }
              break;
          }
        }
      }
      listObject.add(jsonObject);
    }
    return listObject;
  }

  public String getStringCellValue(Cell cell) {
    cell.setCellType(CellType.STRING);
    return cell.getStringCellValue() + "";
  }


  //模板的格式和edit输入框一致
  public Workbook createExcelTemplate(BoxModel boxModel) {
    Workbook wb = new HSSFWorkbook();
    //基本信息
    Sheet sheet = wb.createSheet(boxModel.getBox().name());
    sheet.setZoom(160);
    //冻结首行
    sheet.createFreezePane(0, 1, 1, 1);
    Row headRow = sheet.createRow(0);
    int cellNum = 0;
    for (BoxFieldModel fieldModel : boxModel.getEruptFieldModels()) {
      Edit edit = fieldModel.getUiField().edit();
      if (edit.show() && !edit.readonly().add() && StringUtils.isNotBlank(edit.title())
        && AnnotationUtil.getEditTypeMapping(edit.type()).excelOperator()) {
        Cell cell = headRow.createCell(cellNum);
        //256表格一个字节的宽度
        sheet.setColumnWidth(cellNum, (edit.title().length() + 10) * 256);
        DataValidationHelper dvHelper = sheet.getDataValidationHelper();
        switch (edit.type()) {
          case BOOLEAN:
            sheet.addValidationData(generateValidation(cellNum, SIMPLE_CELL_ERR, DVConstraint.createExplicitListConstraint(new String[]{edit.boolType().trueText(),
              edit.boolType().falseText()})));
            break;
          case CHOICE:
            List<VLModel> vls = BoxUtil.getChoiceList(fieldModel.getUiField().edit().choiceType());
            String[] arr = new String[vls.size()];
            long length = 0;
            for (int i = 0; i < vls.size(); i++) {
              arr[i] = vls.get(i).getLabel();
              length += arr[i].length();
            }
            //下拉框不允许超过255字节
            if (length <= 255) {
              sheet.addValidationData(generateValidation(cellNum, SIMPLE_CELL_ERR, DVConstraint.createExplicitListConstraint(arr)));
            }
            break;
          case SLIDER:
            sheet.addValidationData(generateValidation(cellNum,
              "请选择或输入有效的选项，区间：" + edit.sliderType().min() + " - " + edit.sliderType().max(), dvHelper.createNumericConstraint(
                DataValidationConstraint.ValidationType.INTEGER, DataValidationConstraint.OperatorType.BETWEEN,
                Integer.toString(edit.sliderType().min()), Integer.toString(edit.sliderType().max()))));
            break;
          case DATE:
            if (fieldModel.getFieldReturnName().equals(Date.class.getSimpleName())) {
              sheet.addValidationData(generateValidation(cellNum, "请选择或输入有效时间！"
                , dvHelper.createDateConstraint(DVConstraint.OperatorType.BETWEEN
                  , "1900-01-01", "2999-12-31", "yyyy-MM-dd")));
            }
            break;
          default:
            break;
        }
        //单元格格式
        CellStyle style = wb.createCellStyle();
        style.setLocked(true);
        Font font = wb.createFont();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        font.setBold(true);
        if (edit.notNull()) {
          font.setColor(Font.COLOR_RED);
        }
        if (edit.type() == EditType.REFERENCE_TREE || edit.type() == EditType.REFERENCE_TABLE) {
          style.setFillForegroundColor(IndexedColors.YELLOW1.index);
          style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        style.setFont(font);
        cell.setCellStyle(style);
        //值
        cell.setCellValue(fieldModel.getUiField().edit().title());
        cellNum++;
      }
    }
    return wb;
  }

  private DataValidation generateValidation(int colIndex, String errHint, DataValidationConstraint constraint) {
    // 设置数据有效性加载在哪个单元格上。
    // 四个参数分别是：起始行、终止行、起始列、终止列
    CellRangeAddressList regions = new CellRangeAddressList(1, 1000, colIndex, colIndex);
    DataValidation dataValidationList = new HSSFDataValidation(regions, constraint);
    dataValidationList.createErrorBox("错误", errHint);
    return dataValidationList;
  }

}
