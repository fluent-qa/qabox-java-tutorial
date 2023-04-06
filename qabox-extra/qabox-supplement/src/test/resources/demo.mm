
<map>
  <node ID="root" TEXT="PCF Beta v0.2测试用例">
    <node TEXT="第一周期P0" ID="df127092b3bb0840fe2f87725fa052e2" STYLE="bubble" POSITION="right">
      <node TEXT="产品信息Tab" ID="f42247ba659f76d4f7bf5e542c87cc13" STYLE="fork">
        <node TEXT="删除【产品类型】字段" ID="1276b3703a0a48233991b7d26be67740" STYLE="fork">
          <node TEXT="1、编辑存量产品的基本信息" ID="26bd3c036c84d7dca640e78b1225e09e" STYLE="fork">
            <node TEXT="产品信息页面不展示【产品类型】字段" ID="c2fbb8ccca85ec465d15607937425707" STYLE="fork"/>
            <node TEXT="编辑成功" ID="408af9f89558880de5521531a47fde8f" STYLE="fork"/>
          </node>
          <node TEXT="2、新增产品，编辑基本信息" ID="dc1e48c039b0e09db7f722a395a73b9c" STYLE="fork">
            <node TEXT="新建产品弹窗不展示【产品类型】字段" ID="6a7633128e6b45e0a8ba22df658d7d12" STYLE="fork"/>
            <node TEXT="新建成功" ID="78cb676dbdeace1cab3de2e4151be1aa" STYLE="fork"/>
          </node>
          <node TEXT="3、导出存量分析报告的Word报告，覆盖B to C 和 B to B产品" ID="3c67aca1e99806205d3d5f0ced07949b" STYLE="fork">
            <node TEXT="章节2.2均输出“声明单位”文案" ID="8293f603e7cae27d5ed49b2656a2030f" STYLE="fork"/>
          </node>
          <node TEXT="4、导出增量产品的word报告" ID="da661039a724e4321a7872e444c95708" STYLE="fork">
            <node TEXT="章节2.2固定输出“声明单位”文案" ID="0463b5e8a24421f71fb5eaf461e02322" STYLE="fork"/>
          </node>
        </node>
        <node TEXT="新增【系统边界】字段" ID="ed3407b5d585bd16a659a049dcd6f25e" STYLE="fork">
          <node TEXT="1、查看存量产品的基本信息页面" ID="271253e123a2d170764eb61bb24818ef" STYLE="fork">
            <node TEXT="页面展示【系统边界】字段，回显“从摇篮到大门”" ID="414b4227ae29a4c0fe35b38ee6f5a191" STYLE="fork"/>
            <node TEXT="数据库字段刷值为“从摇篮到大门”" ID="8beb9e95e8c6db6f03aa65c341a9fc46" STYLE="fork"/>
          </node>
          <node TEXT="2、编辑存量产品的【系统边界】字段" ID="bcd5031b06410863a606597423b33151" STYLE="fork">
            <node TEXT="可修改成功" ID="734075d6ee6264dccbd9a65237f1251e" STYLE="fork"/>
          </node>
          <node TEXT="3、新增产品，维护产品的基本信息" ID="a1b5d16c68552ba37a2e14b9e10919ae" STYLE="fork">
            <node TEXT="【系统边界】字段默认选中“从摇篮到大门”" ID="ad37b0e2f8dfbfbebcf4af345aab0956" STYLE="fork"/>
            <node TEXT="【系统边界】下拉列表展示“从摇篮到大门”、“从摇篮到坟墓”" ID="bacafcf614b50fb79ed8bef55d4d03cd" STYLE="fork"/>
            <node TEXT="可新建成功" ID="b6040478d73fb3663b34eb6dba31bc8d" STYLE="fork"/>
          </node>
          <node TEXT="4、查看存量分析报告" ID="3d7662f3e31b158c98770ec27628b747" STYLE="fork">
            <node TEXT="【边界定义】模块展示【系统边界】字段，回显“从摇篮到大门”" ID="42827fade0d37152de158f06eac26109" STYLE="fork"/>
          </node>
          <node TEXT="5、修改存量分析报告" ID="f7c83a11ebd47d5d39ba496d1409b31c" STYLE="fork">
            <node TEXT="【系统边界】字段可以重新编辑成功，下拉列表枚举同产品信息Tab" ID="df3da60b7be1ea455cac6657161f9151" STYLE="fork"/>
          </node>
          <node TEXT="6、新建分析报告" ID="ad49ef3e38827efb312f145c635c1211" STYLE="fork">
            <node TEXT="【系统边界】字段默认为产品信息填写界面时选择的枚举值" ID="f4a02fc9c0fb83fb3beea16446730053" STYLE="fork"/>
            <node TEXT="【系统边界】字段可以重新编辑成功，下拉列表枚举同产品信息Tab" ID="683f39f13b19c58046359957eb206d8e" STYLE="fork"/>
          </node>
          <node TEXT="7、导出word报告" ID="3ca2457a4f22b049aff965f5a1a19163" STYLE="fork">
            <node TEXT="章节4.2和章节6.2新增${product_boundary_type|TEXT}，导出正确" ID="b8f078c9e73d72a0cc624203550074ea" STYLE="fork"/>
          </node>
        </node>
        <node TEXT="修改【基准流】字段为必填" ID="5b1db9ae922a67d85687b48227e4a8c0" STYLE="fork">
          <node TEXT="1、新建产品弹窗的必填校验" ID="2d4fda21fe3638b8154cf6f5cf2c0f2e" STYLE="fork">
            <node TEXT="不维护【基准流】字段直接提交，新建失败，提示必填“请输入基准流”" ID="bc06b2f2f73dcaffb5e44a6b99370f1a" STYLE="fork"/>
            <node TEXT="维护【基准流】及其他必填字段后提交，新建成功，弹窗关闭，进入详情页" ID="724a6398e496affbab77b79f2c835b2a" STYLE="fork"/>
          </node>
          <node TEXT="2、存量数据的必填校验" ID="234baf383b670adeb78b0d41ddd30d72" STYLE="fork">
            <node TEXT="直接保存【基准流】字段未填的存量产品信息，修改失败，提示必填“请输入基准流”" ID="3781bc261027e46cadc669fe1f008239" STYLE="fork"/>
            <node TEXT="清除存量产品的【基准流】字段内容后提交，修改失败，提示必填“请输入基准流”" ID="54e1a532f9ff740d3df548bc4366b4af" STYLE="fork"/>
            <node TEXT="维护存量产品的【基准流】字段后提交修改，修改成功" ID="5a0700f9542014a3ef89dcd8da976f3b" STYLE="fork"/>
          </node>
        </node>
      </node>
      <node TEXT="产品模型Tab" ID="c218afa3d0292ee295ae24886cc4299a" STYLE="fork">
        <node TEXT="修改【排放源名称】字段长度限制" ID="451a1fe78cb5f0a05006278e6273166c" STYLE="fork">
          <node TEXT="1、编辑存量排放源名称" ID="d6b056c65fb26494a9caae393cbfc6d9" STYLE="fork">
            <node TEXT="维护【排放源名称】超过100字段，提示“排放源名称不能超过100个字符”，修改失败" ID="9ab1e51ae253dd57c2d5e6eda65c3d3f" STYLE="fork"/>
            <node TEXT="删除超长部分，提示消失，可修改成功" ID="a169091b9a7db371bc6ec5af81bf0f7e" STYLE="fork"/>
          </node>
          <node TEXT="2、新建排放源，编辑名称" ID="8c5ff2b8a60a2000357513b10259460a" STYLE="fork">
            <node TEXT="维护【排放源名称】超过100字段，提示“排放源名称不能超过100个字符”，修改失败" ID="b1d9a4f729618c904ce3b045a18f45e9" STYLE="fork"/>
            <node TEXT="删除超长部分，提示消失，可修改成功" ID="be683de8dedf8f573c77fbd84105104a" STYLE="fork"/>
          </node>
          <node TEXT="3、名称超长展示" ID="0e67b7055d6e628449ecf7afb635cbb0" STYLE="fork">
            <node TEXT="展示1行，超长截断" ID="6ba1a5b30dc70399dd9a5c4cb6330344" STYLE="fork"/>
          </node>
        </node>
      </node>
      <node TEXT="计算Tab" ID="202c919852fea2cda78a26ed281002e5" STYLE="fork">
        <node TEXT="修改【计算名称】字段长度限制" ID="d7280c1726da6595bc568e88e70e7aa8" STYLE="fork">
          <node TEXT="1、编辑存量计算的计算名称" ID="35124276fe4925ddfcbf06ae3293f04f" STYLE="fork">
            <node TEXT="维护【计算名称】超过100字段，提示“计算名称不能超过100个字符”，保存草稿失败，发起计算失败" ID="203e5db2dd8e343bbcead2ec46f17480" STYLE="fork"/>
            <node TEXT="删除超长部分，提示消失，保存草稿成功，发起计算成功" ID="a50c87d704bd3a8dea9e04df3c07c976" STYLE="fork"/>
          </node>
          <node TEXT="2、直接发起计算，编辑名称" ID="537a9fb1b6f10a194a9a8f1d3fe85218" STYLE="fork">
            <node TEXT="维护【计算名称】超过100字段，提示“计算名称不能超过100个字符”，保存草稿失败，发起计算失败" ID="876ea315c65f5444cc9b30f83715e96e" STYLE="fork"/>
            <node TEXT="删除超长部分，提示消失，保存草稿成功，发起计算成功" ID="6004ea69a3bc936fea186d710054c3c4" STYLE="fork"/>
          </node>
          <node TEXT="3、复制计算，编辑名称" ID="dadcddddb0b63b5e4a1012a49b37b66f" STYLE="fork">
            <node TEXT="维护【计算名称】超过100字段，提示“计算名称不能超过100个字符”，提交失败" ID="74a03b137cae405c498b9b0df17b7664" STYLE="fork"/>
            <node TEXT="删除超长部分，提示消失，提交成功" ID="23175b4b3070efa2f42b56305781d754" STYLE="fork"/>
          </node>
          <node TEXT="4、名称超长展示" ID="d793dc4ff56afb543cf0b338cd7df88c" STYLE="fork">
            <node TEXT="展示1行，超长截断" ID="6ce1003c727790a1cb71de15d6a19756" STYLE="fork"/>
          </node>
        </node>
        <node TEXT="修改发起计算校验逻辑" ID="5c1da75452e3431522bd21284698cfac" STYLE="fork">
          <node TEXT="1、存在未关联单元过程的阶段" ID="b8150f7015c000724109dc761ab5f60b" STYLE="fork">
            <node TEXT="可成功发起计算" ID="bb66295140b58471a136140715f563a1" STYLE="fork"/>
            <node TEXT="结果页（树、饼图、列表）不展示该阶段" ID="35f24b3fa7f87864d9422bf1f9f3aa4a" STYLE="fork"/>
            <node TEXT="分析报告页不展示该阶段" ID="ee4e0b43855ead28c3afdde7a7ed671d" STYLE="fork"/>
          </node>
          <node TEXT="2、存在未关联排放源的单元过程" ID="979f1fc688550469625c9c60a7e36fc7" STYLE="fork">
            <node TEXT="可成功发起计算" ID="360bc30c93b7eee7bf20b7c646988f13" STYLE="fork"/>
            <node TEXT="结果页（树、饼图、列表）不展示该单元过程" ID="75406c2f11a4ea23fbcf73ef6eee2023" STYLE="fork"/>
            <node TEXT="分析报告页不展示该单元过程" ID="8e3e7d2731ffa613fdfeed155d85bf86" STYLE="fork"/>
          </node>
          <node TEXT="3、存在阶段下所有单元过程都未关联排放源" ID="b6aa80b8d25eb6488f7f1af73e8e826a" STYLE="fork">
            <node TEXT="可成功发起计算" ID="33e38311ce9f52c2c03bfb91e6c0e728" STYLE="fork"/>
            <node TEXT="结果页（树、饼图、列表）不展示该阶段" ID="3b2606ce41b804ed14d7f3a9910d9518" STYLE="fork"/>
            <node TEXT="分析报告页不展示该阶段" ID="51fcab215aab34c9be5a1d7469bd45b6" STYLE="fork"/>
          </node>
          <node TEXT="4、模型中不包含任一排放源" ID="6ac81b8bb31babbc25831ce1f2afeb69" STYLE="fork">
            <node TEXT="无法发起计算" ID="c62300ae7d712cb57468bc6d5a3fb929" STYLE="fork"/>
          </node>
        </node>
        <node TEXT="修改【核算周期】字段为时间区间选择器" ID="d60fac86aa1fbd85c623097088cb9e62" STYLE="fork">
          <node TEXT="1、维护核算周期" ID="e6a78b1517dbc2e057441c7dc1e312b9" STYLE="fork">
            <node TEXT="粒度到日" ID="f1dbe6d7b224f566ef06c1bdbba31d3d" STYLE="fork"/>
            <node TEXT="开始日期和结束日期可以选为同一天" ID="7227acfc222d3bf5bddde68888a82b19" STYLE="fork"/>
            <node TEXT="切换年、月、日成功" ID="eb355626493d4db127150a321515a0e9" STYLE="fork"/>
            <node TEXT="选中后正确回显" ID="e92f17d2abf84b1bf288c4f0541c44e3" STYLE="fork"/>
          </node>
          <node TEXT="2、存量数据处理" ID="354df8ee66798e59272138ee5535b95f" STYLE="fork">
            <node TEXT="数据库字段默认刷为2021-01-01 —— 2021-12-31" ID="742f560dc98e153c1a20f2fb18e1476d" STYLE="fork"/>
            <node TEXT="存量数据页面，时间选择器回显2021-01-01 —— 2021-12-31" ID="62ea8d414782b92a22b9648eba28cecd" STYLE="fork"/>
          </node>
        </node>
        <node TEXT="“等价规则”改“无分配”" ID="01f9da03254245e0a813d16ab5f6c39f" STYLE="fork">
          <node TEXT="1、增量活动数据选择默认选中逻辑" ID="e48826f034b9aabd22f0bc00c730ac8e" STYLE="fork">
            <node TEXT="默认选中“无分配”" ID="deb841670d3887e867401b82449c5bfc" STYLE="fork"/>
          </node>
          <node TEXT="2、打开转化规则选择弹窗时默认展示" ID="1d7488704a6cabef1e28457d5da7e60e" STYLE="fork">
            <node TEXT="列表首页首行展示“无分配”" ID="499d689a56c79da58f3dc1f22e547dde" STYLE="fork"/>
          </node>
          <node TEXT="3、搜索转化规则的结果列表" ID="e7ae8055afaec9e3d4dac920da395355" STYLE="fork">
            <node TEXT="有匹配结果，结果列表展示匹配结果" ID="ff790ec13ff8eff8d795b520bfa85dc1" STYLE="fork"/>
            <node TEXT="无匹配结果，结果列表展示空" ID="6d939cb491d62539378731ec6852c6ed" STYLE="fork"/>
            <node TEXT="直接搜索“无分配”，结果列表展示空" ID="3035d1c8353496b132bb3ed8c527d463" STYLE="fork"/>
          </node>
        </node>
        <node TEXT="去除核算单位数量的不确定性" ID="72e115cfa40b37a05c1118baf4f2c9a0" STYLE="fork">
          <node TEXT="1、排放源配置弹窗展示和维护" ID="478a927b157f685de7b7b1da67ceb49c" STYLE="fork">
            <node TEXT="不展示核算单位数量的不确定性字段" ID="773a1a961d4ffa57e73cff68f969b074" STYLE="fork"/>
            <node TEXT="保存存量草稿，不会落库核算单位数量的不确定性字段" ID="08aae201302f01c568144f11135af8dc" STYLE="fork"/>
          </node>
          <node TEXT="2、批量录入弹窗展示和维护" ID="7b541491a1633d3ccbaaf2636e5c66dc" STYLE="fork">
            <node TEXT="不展示核算单位数量的不确定性字段" ID="1f15d18080d89ddacb7e9b3c1e9c39f4" STYLE="fork"/>
            <node TEXT="保存存量草稿，不会落库核算单位数量的不确定性字段" ID="c387774a5f4b50898626a0b7d4233bef" STYLE="fork"/>
          </node>
          <node TEXT="3、排放源列表的展示" ID="1462a1674f6afa65aa6b3c51f5dbe039" STYLE="fork">
            <node TEXT="不展示核算单位数量的不确定性字段" ID="cc352672b737dd2a40de819491cdba1f" STYLE="fork"/>
          </node>
          <node TEXT="4、结果页参数列表的展示" ID="162682236a0dd10ea51a5d5572a256d6" STYLE="fork">
            <node TEXT="不展示核算单位数量的不确定性字段" ID="c747a0a4f398cd912c69e75c8e1d6bb0" STYLE="fork"/>
          </node>
          <node TEXT="5、计算" ID="17cda63b3ce6a777749d1f553327d476" STYLE="fork">
            <node TEXT="存量草稿发起计算，排放源的不确定性不计算核算单位数量的不确定性字段" ID="3b88301ea593d7087b736ae75f8f6fce" STYLE="fork"/>
            <node TEXT="直接发起计算，排放源的不确定性不计算核算单位数量的不确定性字段" ID="eed5d69008059c8db2013c8becf59270" STYLE="fork"/>
            <node TEXT="复制存量计算，发起计算，排放源的不确定性不计算核算单位数量的不确定性字段" ID="5d1fb0236e479eccbcdbeea72e4ec947" STYLE="fork"/>
          </node>
          <node TEXT="6、Excel导出" ID="539f670faaa6802a6eb152c9063a692d" STYLE="fork">
            <node TEXT="sheet5.1不展示【分配数值的不确定性】列" ID="2d2655c9dc6f8fb4f38036d69894d4a6" STYLE="fork"/>
          </node>
        </node>
      </node>
      <node TEXT="分析报告Tab" ID="e6b903ac720fec5f05a7945a1876bdff" STYLE="fork">
        <node TEXT="新增【截断后产品总排放】字段" ID="ffae55b3ca23358466c58ce3c9a1addb" STYLE="fork">
          <node TEXT="展示" ID="cf25254cdcdf425c70fda863020c0995" STYLE="fork">
            <node TEXT="正确回显截断后产品总排放的数值+单位" ID="0a1c15683624fc59ff08936e3c4aae3b" STYLE="fork"/>
            <node TEXT="不可编辑" ID="294590927fe1731fc5d0a01600ebf710" STYLE="fork"/>
          </node>
        </node>
        <node TEXT="新增【查看详情】按钮" ID="833fc104e735d6fb78494faa4084396e" STYLE="fork">
          <node TEXT="展示" ID="f88edbefd17c5bd23893a960e4d31e66" STYLE="fork">
            <node TEXT="点击按钮后，出弹窗展示结果" ID="d00ed5efb4f690095802ba56f7fbdeee" STYLE="fork"/>
            <node TEXT="结果树、饼图、列表过滤掉排除项，排放占比为截断后重新计算结果" ID="3983830c5e2ca8b45af2c60a43c3ab33" STYLE="fork"/>
            <node TEXT="结果树、饼图、列表过滤掉不包含排放源的单元过程、不包含单元过程的阶段" ID="7e7d03fcceaf612bd3c1ee1689ab5f06" STYLE="fork"/>
          </node>
        </node>
        <node TEXT="修改【波动区间】默认值" ID="10accdd1779c01c31ea9fc733ae80e91" STYLE="fork">
          <node TEXT="展示" ID="240d3412301a50c1dfadf7afbebc1def" STYLE="fork">
            <node TEXT="默认值展示为25%" ID="d400f2fbe32bf06fb79939d5fa3fa7f2" STYLE="fork"/>
          </node>
        </node>
        <node TEXT="新增波动区间提示文案" ID="c7a89b3c3e5a7bbc350fcf6ff4de5c7d" STYLE="fork">
          <node TEXT="展示" ID="8e282a2f60bbab9a35dc0baf8fd13bfc" STYLE="fork">
            <node TEXT="展示文案“《ISO 14044:2006 环境管理-产品寿命周期评价-要求和导则标准》建议取值： 25%；”" ID="1e3d3e543b0fb5c74d1475c9357ff090" STYLE="fork"/>
          </node>
        </node>
      </node>
      <node TEXT="转化规则" ID="dcdf746dd2e996ff91d1880889bc1867" STYLE="fork">
        <node TEXT="新增【归档】" ID="bab90133635394bc28a57073b8f89134" STYLE="fork">
          <node TEXT="1、归档转化规则" ID="841c0d2e496254b3d051b18a5e8e225f" STYLE="fork">
            <node TEXT="点击【归档】按钮，出弹窗提示：是否将“xxxxx”数据转化规则归档" ID="e11c5bafb0e5147672c9f625e7d6bbe0" STYLE="fork"/>
            <node TEXT="点击【确认】按钮，弹窗关闭，转化规则列表刷新，不展示被归档的数据" ID="8c122684b64bda0c240a62b44dd79405" STYLE="fork"/>
            <node TEXT="点击【取消】/关闭按钮，弹窗关闭" ID="4e849f75753a004ca4f2a8302fe8ff15" STYLE="fork"/>
          </node>
          <node TEXT="2、已归档列表-展示顺序" ID="3cd7c42cad476c7c7f8d55cd8083e5e6" STYLE="fork">
            <node TEXT="按照归档确认时间降序排列" ID="a5910f769d5e381a3625e8721aa16928" STYLE="fork"/>
          </node>
          <node TEXT="3、已归档列表-归档时间" ID="5359e82317e41d5ba7bd04a426b82654" STYLE="fork">
            <node TEXT="展示归档确认时时间，精确到秒" ID="76ebbc2cd0a4327b191cefc665ae5c62" STYLE="fork"/>
          </node>
          <node TEXT="4、已归档列表-复原" ID="3cf8dd583e448f0f43929590cb193928" STYLE="fork">
            <node TEXT="点击【复原】按钮，提示文案:“是否确认把转化规则「xxx」复原“" ID="bdae38482cba197c95266718ac28b7d7" STYLE="fork"/>
            <node TEXT="点击【确认】按钮，toast 提示「复原成功]，刷新当前列表，展示被复原的数据" ID="a8c4bdaeb1e82c00b34484af895ce16b" STYLE="fork"/>
            <node TEXT="点击【取消】/关闭按钮，隐藏提示，停留在当前页面" ID="450ee68fcc31ab0c328cd3e5cdedef02" STYLE="fork"/>
          </node>
          <node TEXT="5、查看转化规则被归档的计算草稿" ID="57735fd95ab38c14512a8f3f5c823a55" STYLE="fork">
            <node TEXT="活动数据回显空" ID="01747eb1874316fec052facba38a5c31" STYLE="fork"/>
            <node TEXT="排放源展示为不可计算状态，无法发起计算" ID="0a807ecd7d16cba1e2d6eb97e234ec3e" STYLE="fork"/>
          </node>
        </node>
      </node>
      <node TEXT="matrix" ID="9773287a6d2c76ea1d5cfd45f566e104" STYLE="fork">
        <node TEXT="新增除法校验逻辑" ID="4526555901de028ddfe4c03e5a56c9e6" STYLE="fork">
          <node TEXT="1、创建存在除数为元件变量的公式" ID="7f3c703bcb19eb27a6b07b93e7903d2b" STYLE="fork">
            <node TEXT="创建失败，提示" ID="1093b531ca201261eebf2275cc4800a1" STYLE="fork"/>
          </node>
          <node TEXT="2、创建存在除数为活动数据变量的公式" ID="fe1110c455dc785c231d4b7f16f71854" STYLE="fork">
            <node TEXT="创建失败，提示" ID="dae49dc6f110c72eb019b280d2936b4c" STYLE="fork"/>
          </node>
          <node TEXT="3、创建存在除数由变量数据计算来的公式，如C/（A+B）" ID="a47247cb2b91357c7fa8537a48b5303c" STYLE="fork">
            <node TEXT="创建失败，提示" ID="4c8ffc9f3abd31ce1934f63e599e32fe" STYLE="fork"/>
          </node>
          <node TEXT="4、创建所有除数仅为常数的公式" ID="416f6b8dee7263abfaf3449ba81f8202" STYLE="fork">
            <node TEXT="创建成功" ID="e7f18d15458609c21180c12530a0ca5b" STYLE="fork"/>
          </node>
        </node>
      </node>
    </node>
    <node TEXT="第二周期P0" ID="d66fbf75e0774ed2ed2e0e6eb7f309d2" STYLE="bubble" POSITION="right">
      <node TEXT="计算Tab" ID="11d4bfc8d843a03af96ce22e013aa023" STYLE="fork">
        <node TEXT="不确定性打分" ID="027feb720da586bce6a957d22ae334b9" STYLE="fork">
          <node TEXT="1、初级/场地 - 根据逻辑判断命中不确定性数值的项" ID="a19660a94a562310b2f61b7ba5eea447" STYLE="fork">
            <node TEXT="直接回显不确定性数值，不可编辑" ID="3254e0666364bbe96c3c1942417e7311" STYLE="fork"/>
            <node TEXT="直接回显数据质量分数数值，不可编辑" ID="66b34d6d5c6571178a50acd01211e35b" STYLE="fork"/>
            <node TEXT="展示不确定性描述输入框，非必填" ID="e1af4a1f79df9c82cfb1e2fbcf650346" STYLE="fork"/>
          </node>
          <node TEXT="2、初级/场地 - 根据逻辑判断没有不确定性数值的项" ID="37714fbb5fbf52c2ce5414e38b1567e8" STYLE="fork">
            <node TEXT="展示不确定性输入框，必填，0-100" ID="21cc98ef5e3cdfe7c8c1c207b2231089" STYLE="fork"/>
            <node TEXT="增加上传文件按钮，非必填" ID="05e467f3cacaaef7a68489e7f83ee358" STYLE="fork"/>
            <node TEXT="直接回显数据质量分数数值，不可编辑" ID="ee3e5522b6b7729cb2174d2cd1f4f72e" STYLE="fork"/>
            <node TEXT="展示不确定性描述输入框，必填，50字符限制" ID="02c4d09fcb6d5006c2b7fd4121e80d88" STYLE="fork"/>
            <node TEXT="报告导出zip包时，对应排放源的文件夹内增加此处上传的证明文件" ID="e226e921f35724b785deae9dd4d0bcd2" STYLE="fork"/>
          </node>
          <node TEXT="3、初级/场地 - 部分不确定性问题的文案修改" ID="79fb0a8dbdc8b299815bdeddd9e5e0d4" STYLE="fork">
            <node TEXT="" ID="1875228b6f287bfd8ed407ec180b4f34" STYLE="fork"/>
          </node>
          <node TEXT="4、次级 - 新增判断路径" ID="e593032880b52f5280e3fb0ea21dfceb" STYLE="fork">
            <node TEXT="数据类型选择【次级数据】后进入新增的问题弹窗，展示问题为“是否需要通过数据质量打分来确定不确定性？”，展示单选radio “需要”“不需要”" ID="f0d93f65912eedeb9de76f0e896b8c29" STYLE="fork"/>
          </node>
          <node TEXT="5、次级 - 选择【需要】" ID="f05f3871c978a3e1dc1b8b2fea6eb4d7" STYLE="fork">
            <node TEXT="进入数据质量打分弹窗，选择各项评分分数后进入确认分数弹窗" ID="233f9bc282943649a5975bddeb782efe" STYLE="fork"/>
            <node TEXT="确认分数弹窗直接回显不确定性数值，不可编辑" ID="983c00b6e18eb876725f330ed458dc84" STYLE="fork"/>
            <node TEXT="确认分数弹窗直接回显数据质量分数，不可编辑" ID="1851e0eeaa0bcc5539a671632ce1ebe7" STYLE="fork"/>
          </node>
          <node TEXT="6、次级 - 选择【不需要】" ID="93335a26c937cfe1bbe2a43d3f5f60aa" STYLE="fork">
            <node TEXT="直接进入确认分数弹窗" ID="eb9b1a04095b045a143996a6cf8f1466" STYLE="fork"/>
            <node TEXT="展示不确定性输入框，必填，0-100" ID="884ed1f392e3182a36786c8207fbfb3d" STYLE="fork"/>
            <node TEXT="展示不确定性描述输入框，必填，50字符限制" ID="8f2b10157bb81aa95de2238b5286b909" STYLE="fork"/>
            <node TEXT="增加上传文件按钮，非必填" ID="eafc7f1d609c0aaa1ed0f77bb8b0d799" STYLE="fork"/>
            <node TEXT="报告导出zip包时，对应排放源的文件夹内增加此处上传的证明文件" ID="3e48582bee184a48c45b35294762b9fd" STYLE="fork"/>
            <node TEXT="报告导出Excel时，sheet2.1、2.2的【技术代表性】、【地理代表性】、【时间代表性】、【准确性】、【平均分】字段导出为“-”" ID="2f343e96240940a3e5d3f1cf18d41010" STYLE="fork"/>
          </node>
          <node TEXT="7、次级 - 新增数据质量打分的提示文字" ID="77663896cfed6ac88c84afe731c0f575" STYLE="fork">
            <node TEXT="" ID="00cc64acabe35ba153d3ba217dd6ca7a" STYLE="fork"/>
          </node>
          <node TEXT="8、次级 - Excel新增“不确定性描述”" ID="e0b79256fae2cdd491348347ec47549b" STYLE="fork">
            <node TEXT="sheet2.1、2.2的P列（不确定性）后增加列“不确定性描述” ，引用次级数据的不确定性描述字段" ID="2a21563e0c04f5985ebeb355b68b6ce1" STYLE="fork"/>
          </node>
        </node>
        <node TEXT="新建计算" ID="be5a1be1065b2b46d681d47153a06f10" STYLE="fork">
          <node TEXT="新建计算增加新建弹窗" ID="79c74de620e109d0d4dd35725a781585" STYLE="fork">
            <node TEXT="弹窗填写计算名称和核算周期" ID="6a8ffd3ca1b402c2a9e454fe8e25d91e" STYLE="fork"/>
            <node TEXT="确认新建后，进入计算详情页，回显计算名称和核算周期，不可编辑" ID="d7d4017aa0675e6de494670927ebc415" STYLE="fork"/>
          </node>
        </node>
        <node TEXT="活动数据配置" ID="12ec00ea9cd432932a4a61fb318dbf12" STYLE="fork">
          <node TEXT="1、选择转化规则弹窗，搜索功能新增类型筛选" ID="7ae8a06ad29f6edc2c79f7c7c5f539be" STYLE="fork">
            <node TEXT="增量数据，默认选中【无分配】" ID="a55ef14b75d8b258707144721c73dbcc" STYLE="fork"/>
            <node TEXT="存量数据的类型和选项的回显可做，可不做" ID="56f61239969a42159878fd1ff9dea609" STYLE="fork"/>
            <node TEXT="类型筛选和按名称搜索是组合搜索，搜索结果展示交集" ID="14fc91c72804be96fcafed2181b1cc6d" STYLE="fork"/>
            <node TEXT="结果列表移除类型字段，只展示规则名称" ID="8afe8d0d25bb00ebf01af3d4998e9499" STYLE="fork"/>
          </node>
          <node TEXT="2、选择类型为【时间性聚合】或【复合】" ID="f9c19c14af1ca739043db660730a2b57" STYLE="fork">
            <node TEXT="转化规则列表，只展示时间范围在 [核算周期开始时间, 核算周期结束时间] 内的的转化规则" ID="a0d2677bf15a1e97789720ba70154d5f" STYLE="fork"/>
            <node TEXT="转化规则列表上方展示文案：可选的数据记录最大时间区间：「核算周期开始时间」 至 「核算周期结束时间」" ID="caa90a390d83beb7bc0b3ed38bfc9c83" STYLE="fork"/>
          </node>
          <node TEXT="3、选择类型为【无分配】或【分配】" ID="777c7eaa2e872f463802a7dcb911790b" STYLE="fork">
            <node TEXT="数据记录下拉列表，只展示时间范围在 [核算周期开始时间, 核算周期结束时间] 内的的记录" ID="41132a59b5108994938449a9052c95ca" STYLE="fork"/>
            <node TEXT="数据记录选择框下方展示文案：可选的数据记录最大时间区间：「核算周期开始时间」 至 「核算周期结束时间」" ID="8911f3e6a2274bf3250871d0efcf3a1f" STYLE="fork"/>
          </node>
        </node>
      </node>
    </node>
  </node>
</map>