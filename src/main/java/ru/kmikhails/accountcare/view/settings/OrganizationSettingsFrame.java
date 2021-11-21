package ru.kmikhails.accountcare.view.settings;

import ru.kmikhails.accountcare.entity.InspectionOrganization;
import ru.kmikhails.accountcare.service.impl.InspectionOrganizationService;
import ru.kmikhails.accountcare.util.StringUtils;

import javax.swing.*;
import java.util.List;

public class OrganizationSettingsFrame extends AbstractDictionarySettingsFrame {

    private final InspectionOrganizationService inspectionOrganizationService;

    public OrganizationSettingsFrame(InspectionOrganizationService inspectionOrganizationService,
                                     List<String> dictionaryElements, String frameName,
                                     ReconfigureAccountFormListener listener) {
        super(dictionaryElements, frameName, listener);
        this.inspectionOrganizationService = inspectionOrganizationService;
    }

    @Override
    protected void updateElement(String oldValue, String newValue) {
        int index = jList.getSelectedIndex();
        InspectionOrganization inspectionOrganization = inspectionOrganizationService.findByName(oldValue);
        inspectionOrganization = InspectionOrganization.builder()
                .withInspectionOrganization(newValue)
                .withId(inspectionOrganization.getId())
                .build();
        inspectionOrganizationService.update(inspectionOrganization);
        listModel.setElementAt(newValue, index);
    }

    @Override
    protected void deleteElement() {
        String element = jList.getSelectedValue();
        InspectionOrganization inspectionOrganization = inspectionOrganizationService.findByName(element);
        inspectionOrganizationService.deleteById(inspectionOrganization.getId());
        listModel.remove(jList.getSelectedIndex());
    }

    @Override
    protected void addElement(String value) {
        InspectionOrganization inspectionOrganization = InspectionOrganization.builder()
                .withInspectionOrganization(value)
                .build();
        inspectionOrganizationService.save(inspectionOrganization);
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
