package ru.kmikhails.accountcare.view.settings;

import ru.kmikhails.accountcare.entity.Company;
import ru.kmikhails.accountcare.service.impl.CompanyService;

import java.util.List;

public class CompanySettingsFrame extends AbstractDictionarySettingsFrame {

    private final CompanyService companyService;

    public CompanySettingsFrame(CompanyService companyService, List<String> dictionaryElements, String frameName,
                                ReconfigureAccountFormListener listener) {
        super(dictionaryElements, frameName, listener);
        this.companyService = companyService;
    }

    @Override
    protected void updateElement(String oldValue, String newValue) {
        int index = jList.getSelectedIndex();
        Company company = companyService.findByName(oldValue);
        company = Company.builder()
                .withCompany(newValue)
                .withId(company.getId())
                .build();
        companyService.update(company);
        listModel.setElementAt(newValue, index);
    }

    @Override
    protected void deleteElement() {
        String element = jList.getSelectedValue();
        if (element != null) {
            Company company = companyService.findByName(element);
            companyService.deleteById(company.getId());
            listModel.remove(jList.getSelectedIndex());
        }
    }

    @Override
    protected void addElement(String value) {
        Company company = Company.builder()
                .withCompany(value)
                .build();
        companyService.save(company);
        listModel.addElement(value);
    }

    @Override
    public void saveValue(String value) {
        addElement(value);
    }

    @Override
    public void updateValue(String oldValue, String newValue) {
        updateElement(oldValue, newValue);
    }
}
