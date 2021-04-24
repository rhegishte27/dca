import { Button, RadioButton, Select } from 'equisoft-design-ui-elements';
import React, { ChangeEvent, useState } from 'react';
import { useTranslation } from 'react-i18next';
import useCustomForm from '../../../components/form/Form';
import { Buttons } from '../../../components/general/fileexplorer/style';
import { defaultSystemTypeService } from '../../../lib/context';
import BaseEnum from '../../../lib/domain/entities/BaseEnum';
import Location from '../../../lib/domain/entities/Location';
import ProjectSystem from '../../../lib/domain/entities/ProjectSystem';
import System from '../../../lib/domain/entities/System';
import SystemType from '../../../lib/domain/entities/SystemType';
import SystemTypeService from '../../../lib/services/SystemTypeService';
import { LargeForm, OptionCheckboxWrapper, OptionSelectionWrapper, OptionWrapper } from '../../common/style';
import { useProjectSystemSchema } from './useProjectSystemSchema';

interface Props {
    systemTypeService?: SystemTypeService;
    initialValue?: ProjectSystem;
    systemsAvailable: System[];
    locations: Location[];

    onSave(projectSystem: ProjectSystem): void;

    onCancel(): void;
}

const ProjectSystemForm: React.FC<Props> = ({
                                                initialValue,
                                                locations,
                                                systemsAvailable,
                                                systemTypeService = defaultSystemTypeService,
                                                onSave,
                                                onCancel
                                            }) => {

    const { i18n } = useTranslation('common');
    const { fields, validations } = useProjectSystemSchema();

    const [systemTypes, setSystemTypes] = useState<SystemType[]>([]);
    const projectSystem: ProjectSystem = initialValue || new ProjectSystem();

    React.useEffect(() => {
        const loadForm = async () => {
            setSystemTypes(await systemTypeService.findAll());
        };

        loadForm();
    }, []);

    const getInitialValues = (): any => {
        return {
            [fields.system.name]: projectSystem.system.id,
            [fields.systemType.name]: projectSystem.systemType.id,
            [fields.synchronizeSystem.name]: projectSystem.isSynchronizationEnabled.toString(),
            [fields.location.name]: projectSystem.location ? projectSystem.location.id : ''
        };
    };

    const save = async (v: any) => {
        const valueToSave = v.values;
        const projectSystemToSave: ProjectSystem = new ProjectSystem();

        projectSystemToSave.system = systemsAvailable.find(s => s.id.toString() === String(valueToSave[fields.system.name])) || new System();
        projectSystemToSave.systemType = systemTypes.find(s => s.id.toString() === String(valueToSave[fields.systemType.name])) || new SystemType();
        projectSystemToSave.isSynchronizationEnabled = values[fields.synchronizeSystem.name]
            === fields.synchronizeSystem.enum.ENABLE.value;
        projectSystemToSave.location = locations.find(l => l.id.toString() === String(valueToSave[fields.location.name]));

        onSave(projectSystemToSave);
    };

    const {
        values,
        errors,
        handleChange,
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
            { name: fields.synchronizeSystem.name, value: isSync },
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
            <Select
                disabled={!!initialValue}
                label={fields.system.label}
                name={fields.system.name}
                emptySelectText="---"
                options={systemsAvailable.map((s: System) => ({
                    label: s.identifier,
                    value: s.id
                }))}
                value={values[fields.system.name]}
                isValid={!errors[fields.system.name]}
                validationErrorMessage={errors[fields.system.name]}
                onChange={handleChange}
            />
            <br/>
            <Select
                label={fields.systemType.label}
                name={fields.systemType.name}
                emptySelectText="---"
                options={systemTypes.map((s: SystemType) => ({
                    label: i18n.t(BaseEnum.getValue(SystemType, s.id).name),
                    value: s.id
                }))}
                value={values[fields.systemType.name]}
                isValid={!errors[fields.systemType.name]}
                validationErrorMessage={errors[fields.systemType.name]}
                onChange={handleChange}
            />
            <br/>

            <OptionSelectionWrapper>
                <OptionWrapper>{fields.synchronizeSystem.label}</OptionWrapper>
                <OptionCheckboxWrapper>
                    <RadioButton
                        label={fields.synchronizeSystem.enum.ENABLE.label}
                        name={fields.synchronizeSystem.name}
                        value={fields.synchronizeSystem.enum.ENABLE.value}
                        checked={fields.synchronizeSystem.enum.ENABLE.value === values[fields.synchronizeSystem.name]}
                        onChange={handleIsSyncChange}
                    />
                    <RadioButton
                        label={fields.synchronizeSystem.enum.DISABLE.label}
                        name={fields.synchronizeSystem.name}
                        value={fields.synchronizeSystem.enum.DISABLE.value}
                        checked={fields.synchronizeSystem.enum.DISABLE.value === values[fields.synchronizeSystem.name]}
                        onChange={handleIsSyncChange}
                    />
                </OptionCheckboxWrapper>
            </OptionSelectionWrapper>

            <br/>
            <Select
                label={fields.location.label}
                name={fields.location.name}
                emptySelectText="---"
                options={locations.map((s: Location) => ({
                    label: s.identifier,
                    value: s.id
                }))}
                value={values[fields.location.name]}
                isValid={!errors[fields.location.name]}
                validationErrorMessage={errors[fields.location.name]}
                onChange={handleChange}
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

export default ProjectSystemForm;