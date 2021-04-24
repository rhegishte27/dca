import { Select, TextInput } from 'equisoft-design-ui-elements';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import useCustomForm from '../../components/form/Form';
import CancelAndSaveButton from '../../components/general/button/CancelAndSaveButtons';
import FileBrowserInput from '../../components/general/input/fileexplorerinput/FileBrowserInput';
import { defaultLanguageService, defaultSettingService } from '../../lib/context';
import FileData from '../../lib/domain/entities/FileData';
import Language from '../../lib/domain/entities/Language';
import Location from '../../lib/domain/entities/Location';
import LocationType from '../../lib/domain/entities/LocationType';
import Setting from '../../lib/domain/entities/Setting';
import LanguageService from '../../lib/services/LanguageService';
import SettingService from '../../lib/services/SettingService';
import { FormTitle, MediumForm, PanelBreak } from '../common/style';
import { useSettingSchema } from './useSettingSchema';

interface Props {
    settingService?: SettingService;
    languageService?: LanguageService;

    data: Setting

    openMainArea(): void;
}

const SettingForm: React.FC<Props> = ({settingService = defaultSettingService, languageService = defaultLanguageService, openMainArea, data}) => {
    const {i18n} = useTranslation(['common', 'message', 'setting']);
    const {fields, validations} = useSettingSchema();
    const [languageEnum, setLanguageEnum] = React.useState<Language[]>([]);

    React.useEffect(() => {
        loadForm();
    }, []);


    const loadForm = async () => {
        setLanguageEnum(await languageService.findAll());
    };

    const getInitialValues = (): any => {
        const setting = data ? data : new Setting();
        return {
            ...setting.toPlainObj(),
            [fields.language.name]: setting.language ? setting.language.id : ''
        };
    };

    const save = async (v: any) => {
        const settingToSave = prepareToSave(v);
        settingToSave.id ? await settingService.update(settingToSave) : await settingService.add(settingToSave);
    };

    const onSubmit = async (v: any) => {
        await save(v.values);
        toast.success(i18n.t('message:success:save', {what: i18n.t('setting:title')}));
    };

    const prepareToSave = (valuesToSave: any): Setting => {
        const settingToSave: Setting = new Setting();
        settingToSave.applyData(valuesToSave);
        settingToSave.language = languageEnum.find(r => r.id.toString() === String(valuesToSave[fields.language.name])) || new Language();
        return settingToSave;
    };

    const commonFolderChangeHandler = (event: React.ChangeEvent<HTMLInputElement>) => {
        handleMultipleChange([
            {name: fields.commonFolder.name, value: event.target.value},
            {name: fields.defaultImportFolder.name, value: values[fields.defaultImportFolder.name]},
            {name: fields.defaultExportFolder.name, value: values[fields.defaultExportFolder.name]},
            {name: fields.defaultDownloadFolder.name, value: values[fields.defaultDownloadFolder.name]}
        ]);
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
        onSubmit: onSubmit,
        validations,
        redirectAfterSave: openMainArea
    });

    const onCancelClick = () => {
        openMainArea();
    };

    const getPath = (filesChosen: FileData[]): string => {
        let path = '';

        filesChosen.forEach((fileData, index) => {
            if (index !== 0) {
                path = path + ', ';
            }
            path = path + fileData.path;
        });
        return path;
    };

    const getLocation = (path: string): Location => {
        const location: Location = new Location();
        location.locationType = LocationType.NETWORK;
        location.path = path;

        return location;
    };

    return (
        <MediumForm onSubmit={handleSubmit}>
            <FormTitle>{i18n.t('setting:title')}</FormTitle>
            <PanelBreak/>

            <Select
                label={fields.language.label}
                name={fields.language.name}
                emptySelectText="---"
                options={languageEnum.map((g: Language) => ({
                    label: g.name,
                    value: g.id
                }))}
                value={values[fields.language.name]}
                isValid={!errors[fields.language.name]}
                validationErrorMessage={errors[fields.language.name]}
                onChange={handleChange}
            />

            <br/>

            <TextInput
                autoFocus
                type={'number'}
                label={fields.tokenDuration.label}
                name={fields.tokenDuration.name}
                value={values[fields.tokenDuration.name]}
                isValid={!errors[fields.tokenDuration.name]}
                validationErrorMessage={errors[fields.tokenDuration.name]}
                onChange={handleChange}
                onBlur={handleBlur}
            />

            <br/>

            <TextInput
                minLength={fields.path.minSize}
                maxLength={fields.path.maxSize}
                label={fields.commonFolder.label}
                name={fields.commonFolder.name}
                value={values[fields.commonFolder.name]}
                isValid={!errors[fields.commonFolder.name]}
                validationErrorMessage={errors[fields.commonFolder.name]}
                onChange={commonFolderChangeHandler}
                onBlur={handleBlur}
            />

            <br/>

            <FileBrowserInput
                disabled
                typeToShow={['Directory']}
                isCreateAndDeleteFolderAllow
                location={getLocation(values[fields.commonFolder.name] ? values[fields.commonFolder.name] : '')}
                minLength={fields.path.minSize}
                maxLength={fields.path.maxSize}
                name={fields.defaultImportFolder.name}
                handleChange={(filesChosen: FileData[]) => {
                    handleMultipleChange([
                        { name: fields.defaultImportFolder.name, value: getPath(filesChosen) }
                    ]);
                }}
                value={values[fields.defaultImportFolder.name]}
                onBlur={handleBlur}
                label={fields.defaultImportFolder.label}
                validationErrorMessage={errors[fields.defaultImportFolder.name]}
                isBrowseButtonDisable={!!errors[fields.commonFolder.name]}
            />

            <br/>

            <FileBrowserInput
                disabled
                typeToShow={['Directory']}
                isCreateAndDeleteFolderAllow
                location={getLocation(values[fields.commonFolder.name] ? values[fields.commonFolder.name] : '')}
                minLength={fields.path.minSize}
                maxLength={fields.path.maxSize}
                name={fields.defaultExportFolder.name}
                value={values[fields.defaultExportFolder.name]}
                handleChange={(filesChosen: FileData[]) => {
                    handleMultipleChange([
                        { name: fields.defaultExportFolder.name, value: getPath(filesChosen) }
                    ]);
                }}
                onBlur={handleBlur}
                label={fields.defaultExportFolder.label}
                validationErrorMessage={errors[fields.defaultExportFolder.name]}
                isBrowseButtonDisable={!!errors[fields.commonFolder.name]}
            />

            <br/>

            <FileBrowserInput
                disabled
                typeToShow={['Directory']}
                isCreateAndDeleteFolderAllow
                location={getLocation(values[fields.commonFolder.name] ? values[fields.commonFolder.name] : '')}
                minLength={fields.path.minSize}
                maxLength={fields.path.maxSize}
                name={fields.defaultDownloadFolder.name}
                value={values[fields.defaultDownloadFolder.name]}
                handleChange={(filesChosen: FileData[]) => {
                    handleMultipleChange([
                        { name: fields.defaultDownloadFolder.name, value: getPath(filesChosen) }
                    ]);
                }}
                onBlur={handleBlur}
                label={fields.defaultDownloadFolder.label}
                validationErrorMessage={errors[fields.defaultDownloadFolder.name]}
                isBrowseButtonDisable={!!errors[fields.commonFolder.name]}
            />

            <br/>

            <CancelAndSaveButton onCancelClick={onCancelClick}/>

        </MediumForm>
    );
};

export default SettingForm;