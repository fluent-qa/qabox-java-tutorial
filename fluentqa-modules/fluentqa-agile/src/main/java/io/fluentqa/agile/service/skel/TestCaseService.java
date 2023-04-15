package io.fluentqa.agile.service.skel;

public interface TestCaseService {

    public void add(TestCaseDto dto);
    public void update(TestCaseDto dto);

    public void delete(TestCaseDto dto);

    public void getBy(TestCaseDto dto);

    public void search(TestCaseDto dto);

}
