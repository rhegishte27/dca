import { RadioButton, Select, TextAreaInput, TextInput } from 'equisoft-design-ui-elements';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { useMainAreaContext } from '../../components/editor/MainAreaProvider';
import useCustomForm from '../../components/form/Form';
import CancelAndSaveButton from '../../components/general/button/CancelAndSaveButtons';
import {
    defaultBackupIntervalService,
    defaultBackupKeepIntervalService,
    defaultCompilerService,
    defaultGeneratedCodeLanguageService,
    defaultLocationService,
    defaultProjectService
} from '../../lib/context';
import BackupInterval from '../../lib/domain/entities/BackupInterval';
import BackupKeepInterval from '../../lib/domain/entities/BackupKeepInterval';
import BaseEnum from '../../lib/domain/entities/BaseEnum';
import Compiler from '../../lib/domain/entities/Compiler';
import GeneratedCodeLanguage from '../../lib/domain/entities/GeneratedCodeLanguage';
import Location from '../../lib/domain/entities/Location';
import Project from '../../lib/domain/entities/Project';
import ProjectSyncSetting from '../../lib/domain/entities/ProjectSyncSetting';
import ProjectSystem from '../../lib/domain/entities/ProjectSystem';
import BackupIntervalService from '../../lib/services/BackupIntervalService';
import BackupKeepIntervalService from '../../lib/services/BackupKeepIntervalService';
import CompilerService from '../../lib/services/CompilerService';
import GeneratedCodeLanguageService from '../../lib/services/GeneratedCodeLanguageService';
import LocationService from '../../lib/services/LocationService';
import ProjectService from '../../lib/services/ProjectService';
import { useAuthState } from '../../page/authContext';
import { ErrorMessage, FormTitle, MediumForm, OptionCheckboxWrapper, OptionSelectionWrapper, OptionWrapper, PanelBreak, Table } from '../common/style';
import { getTextAreaRows } from '../common/util';
import ProjectSyncSettingList from './projectsyncsettings/ProjectSyncSettingList';
import ProjectSystemList from './projectsystem/ProjectSystemList';
import { useProjectSchema } from './useProjectSchema';

interface Props {
    projectService?: ProjectService;
    generatedCodeLanguageService?: GeneratedCodeLanguageService;
    compilerService?: CompilerService;
    backupIntervalService?: BackupIntervalService;
    backupKeepIntervalService?: BackupKeepIntervalService;
    locationService?: LocationService;

    data?: Project;

    openList(): void;
}

const ProjectForm: React.FC<Props> = ({
                                          projectService = defaultProjectService,
                                          generatedCodeLanguageService = defaultGeneratedCodeLanguageService,
                                          compilerService = defaultCompilerService,
                                          backupIntervalService = defaultBackupIntervalService,
                                          backupKeepIntervalService = defaultBackupKeepIntervalService,
                                          locationService = defaultLocationService,
                                          data, openList
                                      }) => {
    const [generatedCodeLanguageEnum, setGeneratedCodeLanguageEnum] = React.useState<GeneratedCodeLanguage[]>([]);
    const [compilerEnum, setCompilerEnum] = React.useState<Compiler[]>([]);
    const [backupIntervalEnum, setBackupIntervalEnum] = React.useState<BackupInterval[]>([]);
    const [backupKeepIntervalEnum, setBackupKeepIntervalEnum] = React.useState<BackupKeepInterval[]>([]);
    const [locations, setLocations] = React.useState<Location[]>([]);
    const {i18n} = useTranslation(['message', 'project', 'common']);
    const { fields, validations } = useProjectSchema();
    const {openMainArea} = useMainAreaContext();
    const { updateUserSetting } = useAuthState();
    const project: Project = data || new Project();

    React.useEffect(() => {
        loadForm();
    }, []);

    const loadForm = async () => {
        setGeneratedCodeLanguageEnum(await generatedCodeLanguageService.findAll());
        setCompilerEnum(await compilerService.findAll());
        setBackupIntervalEnum(await backupIntervalService.findAll());
        setBackupKeepIntervalEnum(await backupKeepIntervalService.findAll());
        setLocations(await locationService.findAll());
    };

    const getInitialValues = (): any => {
        return {
            ...project.toPlainObj(),
            [fields.generatedCodeLanguage.name]: project.generatedCodeLanguage.id,
            [fields.compiler.name]: project.compiler.id,
            [fields.synchronize.name]: project.isSynchronizationEnabled.toString(),
            [fields.backup.name]: project.isBackupEnabled.toString(),
            [fields.backupWrapper.backupInterval.name]: project.backupInterval ? project.backupInterval.id : null,
            [fields.backupKeepWrapper.backupKeepInterval.name]: project.backupKeepInterval ? project.backupKeepInterval.id : null,
            [fields.synchronizeProjectElements.name]: project.isSynchronizeProjectElementsEnabled.toString()
        };
    };

    const prepareToSave = (valueToSave: any): Project => {
        const projectToSave: Project = new Project();
        projectToSave.applyData(valueToSave);
        projectToSave.generatedCodeLanguage =
            generatedCodeLanguageEnum.find(o => o.id.toString() === String(valueToSave[fields.generatedCodeLanguage.name]))
            || new GeneratedCodeLanguage;

        projectToSave.compiler =
            compilerEnum.find(o => o.id.toString() === String(valueToSave[fields.compiler.name]))
            || new Compiler();

        projectToSave.backupInterval =
            backupIntervalEnum.find(o => o.id.toString() === String(valueToSave[fields.backupWrapper.backupInterval.name]))
            || new BackupInterval();

        projectToSave.backupKeepInterval =
            backupKeepIntervalEnum.find(o => o.id.toString() === String(valueToSave[fields.backupKeepWrapper.backupKeepInterval.name]))
            || new BackupKeepInterval();

        projectToSave.isSynchronizationEnabled =
            valueToSave[fields.synchronize.name] === fields.synchronize.enum.ENABLE.value;

        projectToSave.isBackupEnabled =
            valueToSave[fields.backup.name] === fields.backup.enum.ENABLE.value;

        projectToSave.isSynchronizeProjectElementsEnabled =
            valueToSave[fields.synchronizeProjectElements.name] === fields.synchronizeProjectElements.enum.ENABLE.value;

        return projectToSave;
    };

    const save = async (v: any) => {
        let projectToSave: Project = prepareToSave(v.values);

        if (projectToSave.id) {
            await projectService.update(projectToSave);
        } else {
            projectToSave = await projectService.add(projectToSave);
        }
        toast.success(i18n.t('message:success:save', { what: projectToSave.identifier }));
        updateUserSetting('Project', projectToSave);
        return;
    };

    const {
        values,
        errors,
        handleChange,
        handleMultipleChange,
        handleBlur,
        handleSubmit
    } = useCustomForm({
        initialValues: getInitialValues(),
        onSubmit: async (v: any) => {
            await save(v);
        },
        validations,
        redirectAfterSave: openList
    });

    const onCancelClick = () => {
        openMainArea('ProjectList');
    };

    const generatedCodeLanguageChangeHandler = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const input = e.target.value;
        if (GeneratedCodeLanguage.isJava(input)) {
            handleMultipleChange([
                {name: fields.generatedCodeLanguage.name, value: input},
                {name: fields.compiler.name, value: Compiler.GENERIC.id}
            ]);
        } else {
            handleChange(e);
        }
    };

    const isBackupChangeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
        const input = e.target.value;
        if (isBackupDisable(input)) {
            handleMultipleChange([
                {name: fields.backup.name, value: input},
                /*set all the backup fields to null if backup is disable*/
                {name: fields.backupWrapper.backupInterval.name, value: null},
                {name: fields.backupWrapper.numberBackupInterval.name, value: null},
                {name: fields.backupKeepWrapper.backupKeepInterval.name, value: null},
                {name: fields.backupKeepWrapper.numberBackupKeepInterval.name, value: null}
            ]);
        } else {
            handleMultipleChange([
                {name: fields.backup.name, value: input},
                /*set all the backup fields to default value if backup is enable*/
                {name: fields.backupWrapper.backupInterval.name, value: ''},
                {name: fields.backupWrapper.numberBackupInterval.name, value: ''},
                {name: fields.backupKeepWrapper.backupKeepInterval.name, value: ''},
                {name: fields.backupKeepWrapper.numberBackupKeepInterval.name, value: ''}
            ]);
        }
    };

    const isBackupDisable = (input: string): boolean => {
        return fields.backup.enum.DISABLE.value === input;
    };

    const isEditMode = (): boolean => {
        return values[fields.id.name];
    };

    const getWrapperErrorMessage = (intervalName: string, numberName: string): string => {
        const errorInterval = errors[intervalName];
        const errorNumber = errors[numberName];

        if (errorInterval === fields.validation.required || errorNumber === fields.validation.required) {
            return i18n.t('message:validation:allRequired');
        }

        if (errorNumber === fields.validation.greaterThanZero) {
            return i18n.t('message:validation:greaterThanZero', {what: i18n.t('common:number')});
        }

        return errorInterval || errorNumber;

    };

    const ShowErrorIfExisted = (props: any) => (
        getWrapperErrorMessage(props.nameInterval, props.nameNumber) ?
            <tr>
                <td colSpan={3}>
                    <ErrorMessage>{getWrapperErrorMessage(props.nameInterval, props.nameNumber)}</ErrorMessage>
                </td>
            </tr>
            : null
    );


    return (
        <MediumForm onSubmit={handleSubmit}>
            <FormTitle>{i18n.t('project:titlePlural')}</FormTitle>
            <PanelBreak/>

            <TextInput
                autoFocus={!isEditMode()}
                disabled={isEditMode()}
                label={fields.identifier.label}
                name={fields.identifier.name}
                minLength={fields.identifier.minSize}
                maxLength={fields.identifier.maxSize}
                isValid={!errors[fields.identifier.name]}
                validationErrorMessage={errors[fields.identifier.name]}
                value={values[fields.identifier.name]}
                onChange={handleChange}
                onBlur={handleBlur}
            />
            <br/>
            <TextAreaInput
                autoFocus={isEditMode()}
                label={fields.description.label}
                name={fields.description.name}
                minLength={fields.description.minSize}
                maxLength={fields.description.maxSize}
                isValid={!errors[fields.description.name]}
                validationErrorMessage={errors[fields.description.name]}
                value={values[fields.description.name]}
                rows={getTextAreaRows(values[fields.description.name])}
                onChange={handleChange}
                onBlur={handleBlur}
            />
            <br/>

            <Select
                label={fields.generatedCodeLanguage.label}
                name={fields.generatedCodeLanguage.name}
                emptySelectText="---"
                options={generatedCodeLanguageEnum.map((g: GeneratedCodeLanguage) => ({
                    label: g.name,
                    value: g.id
                }))}
                value={values[fields.generatedCodeLanguage.name]}
                isValid={!errors[fields.generatedCodeLanguage.name]}
                validationErrorMessage={errors[fields.generatedCodeLanguage.name]}
                onChange={generatedCodeLanguageChangeHandler}
            />
            <br/>
            <Select
                label={fields.compiler.label}
                name={fields.compiler.name}
                emptySelectText="---"
                options={compilerEnum.map((c: Compiler) => ({
                    label: i18n.t(BaseEnum.getValue(Compiler, c.id).name),
                    value: c.id
                }))}
                value={values[fields.compiler.name]}
                isValid={!errors[fields.compiler.name]}
                validationErrorMessage={errors[fields.compiler.name]}
                disabled={GeneratedCodeLanguage.isJava(values[fields.generatedCodeLanguage.name])}
                onChange={handleChange}
            />

            <br/>

            <OptionSelectionWrapper>
                <OptionWrapper>{fields.backup.label}</OptionWrapper>
                <OptionCheckboxWrapper>
                    <RadioButton
                        label={fields.backup.enum.ENABLE.label}
                        name={fields.backup.name}
                        value={fields.backup.enum.ENABLE.value}
                        checked={fields.backup.enum.ENABLE.value === values[fields.backup.name]}
                        onChange={isBackupChangeHandler}
                    />
                    <RadioButton
                        label={fields.backup.enum.DISABLE.label}
                        name={fields.backup.name}
                        value={fields.backup.enum.DISABLE.value}
                        checked={fields.backup.enum.DISABLE.value === values[fields.backup.name]}
                        onChange={isBackupChangeHandler}
                    />
                </OptionCheckboxWrapper>
            </OptionSelectionWrapper>

            <br/>

            <div hidden={isBackupDisable(values[fields.backup.name])}>
                <Table>
                    <tbody>
                    <ShowErrorIfExisted
                        nameInterval={fields.backupWrapper.backupInterval.name}
                        nameNumber={fields.backupWrapper.numberBackupInterval.name}
                    />
                    <tr>
                        <td><label>{fields.backupWrapper.label}</label></td>
                        <td>
                            <TextInput
                                type={'number'}
                                name={fields.backupWrapper.numberBackupInterval.name}
                                value={values[fields.backupWrapper.numberBackupInterval.name]}
                                onChange={handleChange}
                                onBlur={handleBlur}
                                min={1}
                            />
                        </td>
                        <td>
                            <Select
                                name={fields.backupWrapper.backupInterval.name}
                                emptySelectText="---"
                                options={backupIntervalEnum.map((b: BackupInterval) => ({
                                    label: i18n.t(BaseEnum.getValue(BackupInterval, b.id).name),
                                    value: b.id
                                }))}
                                value={values[fields.backupWrapper.backupInterval.name]}
                                onChange={handleChange}
                            />
                        </td>
                    </tr>

                    {/*Create a space between 2 fields*/}
                    <tr>
                        <td>&nbsp;</td>
                    </tr>

                    <ShowErrorIfExisted
                        nameInterval={fields.backupKeepWrapper.backupKeepInterval.name}
                        nameNumber={fields.backupKeepWrapper.numberBackupKeepInterval.name}
                    />
                    <tr>
                        <td><label>{fields.backupKeepWrapper.label}</label></td>
                        <td>
                            <TextInput
                                type={'number'}
                                name={fields.backupKeepWrapper.numberBackupKeepInterval.name}
                                value={values[fields.backupKeepWrapper.numberBackupKeepInterval.name]}
                                onChange={handleChange}
                                onBlur={handleBlur}
                                min={1}
                            />
                        </td>
                        <td>
                            <Select
                                name={fields.backupKeepWrapper.backupKeepInterval.name}
                                emptySelectText="---"
                                options={backupKeepIntervalEnum.map((b: BackupKeepInterval) => ({
                                    label: i18n.t(BaseEnum.getValue(BackupKeepInterval, b.id).name),
                                    value: b.id
                                }))}
                                value={values[fields.backupKeepWrapper.backupKeepInterval.name]}
                                onChange={handleChange}
                            />
                        </td>
                    </tr>
                    </tbody>
                </Table>

                <br/>
            </div>

            <OptionSelectionWrapper>
                <OptionWrapper>{fields.synchronizeProjectElements.label}</OptionWrapper>
                <OptionCheckboxWrapper>
                    <RadioButton
                        label={fields.synchronizeProjectElements.enum.ENABLE.label}
                        name={fields.synchronizeProjectElements.name}
                        value={fields.synchronizeProjectElements.enum.ENABLE.value}
                        checked={fields.synchronizeProjectElements.enum.ENABLE.value === values[fields.synchronizeProjectElements.name]}
                        onChange={handleChange}
                    />
                    <RadioButton
                        label={fields.synchronizeProjectElements.enum.DISABLE.label}
                        name={fields.synchronizeProjectElements.name}
                        value={fields.synchronizeProjectElements.enum.DISABLE.value}
                        checked={fields.synchronizeProjectElements.enum.DISABLE.value === values[fields.synchronizeProjectElements.name]}
                        onChange={handleChange}
                    />
                </OptionCheckboxWrapper>
            </OptionSelectionWrapper>

            <br/>

            <ProjectSystemList
                projectSystems={values[fields.projectSystems.name]}
                locations={locations}
                updateProjectSystems={(projectSystems: ProjectSystem[]) => handleMultipleChange([{
                    name: fields.projectSystems.name, value: projectSystems
                }])}
            />

            <br/>

            <ProjectSyncSettingList
                projectSyncSettings={values[fields.projectSyncSettings.name]}
                locations={locations}
                updateProjectSyncSettings={(projectSyncSetting: ProjectSyncSetting[]) => handleMultipleChange([{
                    name: fields.projectSyncSettings.name, value: projectSyncSetting
                }])}
            />

            <br/>

            <CancelAndSaveButton onCancelClick={onCancelClick}/>
        </MediumForm>
    );
};

export default ProjectForm;
