import { Button, RadioButton, Select, TextInput } from 'equisoft-design-ui-elements';
import React, { ChangeEvent } from 'react';
import { useTranslation } from 'react-i18next';
import useCustomForm from '../../../components/form/Form';
import { Buttons } from '../../../components/general/fileexplorer/style';
import BaseEnum from '../../../lib/domain/entities/BaseEnum';
import Location from '../../../lib/domain/entities/Location';
import ProjectSyncSetting from '../../../lib/domain/entities/ProjectSyncSetting';
import TypeProjectElement from '../../../lib/domain/entities/TypeProjectElement';
import { LargeForm, OptionCheckboxWrapper, OptionSelectionWrapper, OptionWrapper } from '../../common/style';
import { useProjectSyncSettingSchema } from './useProjectSyncSettingSchema';

interface Props {
    projectSyncSetting: ProjectSyncSetting;
    locations: Location[];

    onSave(projectSyncSetting: ProjectSyncSetting): void;

    onCancel(): void;
}

const ProjectSyncSettingForm: React.FC<Props> = ({
                                                     projectSyncSetting,
                                                     locations,
                                                     onSave,
                                                     onCancel
                                                 }) => {

    const { i18n } = useTranslation('project');
    const { fields, validations } = useProjectSyncSettingSchema();

    const getInitialValues = (): any => {
        return {
            [fields.synchronizeProjectElement.name]: projectSyncSetting.isSyncEnabled.toString(),
            [fields.location.name]: projectSyncSetting.location ? projectSyncSetting.location.id : ''
        };
    };

    const save = async (v: any) => {
        const valueToSave = v.values;

        projectSyncSetting.isSyncEnabled = values[fields.synchronizeProjectElement.name]
            === fields.synchronizeProjectElement.enum.ENABLE.value;
        projectSyncSetting.location = locations.find(l => l.id.toString() === String(valueToSave[fields.location.name]));

        onSave(projectSyncSetting);
    };

    const {
        values,
        errors,
        handleSubmit,
        handleMultipleChange
    } = useCustomForm({
        initialValues: getInitialValues(),
        validations,
        onSubmit: async (v: any) => {
            await save(v);
        }
    });

    const handleIsSyncChange = (e: ChangeEvent<HTMLInputElement>) => {
        const isSync = e.target.value;
        handleMultipleChange([
            { name: fields.synchronizeProjectElement.name, value: isSync },
            { name: fields.location.name, value: values[fields.location.name] }
        ]);
    };

    return (
        <LargeForm onSubmit={(e) => {
            e.preventDefault();
            handleSubmit(e);
            e.stopPropagation();
        }}>
            <br/>

            <TextInput
                disabled
                label={fields.elementType.label}
                name={fields.elementType.name}
                value={i18n.t(BaseEnum.getValue(TypeProjectElement, projectSyncSetting.typeProjectElement.id).name).toString()}
            />

            <br/>

            <OptionSelectionWrapper>
                <OptionWrapper>{fields.synchronizeProjectElement.label}</OptionWrapper>
                <OptionCheckboxWrapper>
                    <RadioButton
                        label={fields.synchronizeProjectElement.enum.ENABLE.label}
                        name={fields.synchronizeProjectElement.name + projectSyncSetting.typeProjectElement.id}
                        value={fields.synchronizeProjectElement.enum.ENABLE.value}
                        checked={fields.synchronizeProjectElement.enum.ENABLE.value === values[fields.synchronizeProjectElement.name]}
                        onChange={handleIsSyncChange}
                    />
                    <RadioButton
                        label={fields.synchronizeProjectElement.enum.DISABLE.label}
                        name={fields.synchronizeProjectElement.name + projectSyncSetting.typeProjectElement.id}
                        value={fields.synchronizeProjectElement.enum.DISABLE.value}
                        checked={fields.synchronizeProjectElement.enum.DISABLE.value === values[fields.synchronizeProjectElement.name]}
                        onChange={handleIsSyncChange}
                    />
                </OptionCheckboxWrapper>
            </OptionSelectionWrapper>

            <br/>

            <Select
                label={fields.location.label}
                name={fields.location.name + projectSyncSetting.typeProjectElement.id}
                emptySelectText="---"
                options={locations.map((c: Location) => ({
                    label: c.identifier,
                    value: c.id
                }))}
                value={values[fields.location.name]}
                isValid={!errors[fields.location.name]}
                validationErrorMessage={errors[fields.location.name]}
                onChange={e => handleMultipleChange([{ name: fields.location.name, value: e.target.value }])}
            />

            <br/>

            <Buttons>
                <Button buttonType='secondary' onClick={onCancel}>
                    {i18n.t('common:button:cancel')}
                </Button>

                <Button type="submit" buttonType='primary'>
                    {i18n.t('common:button:save')}
                </Button>
            </Buttons>

        </LargeForm>
    );
};

export default ProjectSyncSettingForm;