import { Button, RadioButton, Select } from 'equisoft-design-ui-elements';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { useMainAreaContext } from '../../../components/editor/MainAreaProvider';
import useCustomForm from '../../../components/form/Form';
import FileBrowserInput from '../../../components/general/input/fileexplorerinput/FileBrowserInput';
import FileInput from '../../../components/general/input/fileinput/FileInput';
import {
    defaultDataObjectLocationTypeService,
    defaultDataObjectService,
    defaultDataObjectTypeService,
    defaultLocationService,
    defaultSettingService
} from '../../../lib/context/index';
import BaseEnum from '../../../lib/domain/entities/BaseEnum';
import DataObjectContainer from '../../../lib/domain/entities/DataObjectContainer';
import DataObjectFile from '../../../lib/domain/entities/DataObjectFile';
import DataObjectLocationType from '../../../lib/domain/entities/DataObjectLocationType';
import DataObjectType from '../../../lib/domain/entities/DataObjectType';
import FileData from '../../../lib/domain/entities/FileData';
import Location from '../../../lib/domain/entities/Location';
import LocationType from '../../../lib/domain/entities/LocationType';
import Setting from '../../../lib/domain/entities/Setting';
import DataObjectLocationTypeService from '../../../lib/services/DataObjectLocationTypeService';
import DataObjectService from '../../../lib/services/DataObjectService';
import DataObjectTypeService from '../../../lib/services/DataObjectTypeService';
import LocationService from '../../../lib/services/LocationService';
import SettingService from '../../../lib/services/SettingService';
import { Actions, FormTitle, MediumForm, OptionCheckboxWrapper, OptionSelectionWrapper, OptionWrapper, PanelBreak } from '../../common/style';
import { useDataObjectFormUtils } from '../common/useDataObjectFormUtils';
import { useDataObjectContainerSchema } from './useDataObjectContainerSchema';

interface Props {
    dataObjectService?: DataObjectService;
    dataObjectTypeService?: DataObjectTypeService;
    dataObjectLocationTypeService?: DataObjectLocationTypeService;
    locationService?: LocationService;
    settingService?: SettingService;

    data: DataObjectContainer;

    onCancelClick(): void;
}

const DataObjectContainerForm: React.FC<Props> = ({
                                                      data,
                                                      dataObjectService = defaultDataObjectService,
                                                      dataObjectTypeService = defaultDataObjectTypeService,
                                                      dataObjectLocationTypeService = defaultDataObjectLocationTypeService,
                                                      locationService = defaultLocationService,
                                                      settingService = defaultSettingService,
                                                      onCancelClick
                                                  }) => {
    const { i18n } = useTranslation();
    const { fields, validations } = useDataObjectContainerSchema();
    const [dataObjectContainer, setDataObjectContainer] = React.useState<DataObjectContainer>(data);
    const [dataObjectTypes, setDataObjectTypes] = React.useState<DataObjectType[]>([]);
    const [dataObjectLocationTypes, setDataObjectLocationTypes] = React.useState<DataObjectLocationType[]>([]);
    const [locations, setLocations] = React.useState<Location[]>([]);
    const [setting, setSetting] = React.useState<Setting>(new Setting());
    const { getFileName } = useDataObjectFormUtils();
    const { openMainArea } = useMainAreaContext();

    React.useEffect(() => {
        const loadForm = async () => {
            setDataObjectTypes(await dataObjectTypeService.findAll());
            setDataObjectLocationTypes(await dataObjectLocationTypeService.findAll());
            setLocations(await locationService.findAll());
            setSetting(await settingService.get());
        };

        loadForm();
    }, []);

    const getInitialValues = (): any => {
        return {
            [fields.dataObjectFileList.name]: dataObjectContainer.dataObjectFileList,
            [fields.dataObjectLocationType.name]: dataObjectContainer.locationType ? dataObjectContainer.locationType.id : DataObjectLocationType.MY_COMPUTER.id,
            [fields.dataObjectType.name]: dataObjectContainer.type ? dataObjectContainer.type.id : '',
            [fields.location.name]: dataObjectContainer.location ? dataObjectContainer.location.id : ''
        };
    };

    const prepareContainer = (valueToSave: any): DataObjectContainer => {
        const container: DataObjectContainer = new DataObjectContainer(data.system);
        container.locationType = dataObjectLocationTypes.find(o => o.id.toString() === String(valueToSave[fields.dataObjectLocationType.name]));
        container.type = dataObjectTypes.find(o => o.id.toString() === String(valueToSave[fields.dataObjectType.name]));
        container.location = locations.find(o => o.id.toString() === String(valueToSave[fields.location.name]));
        container.dataObjectFileList = values[fields.dataObjectFileList.name];

        return container;
    };

    const selectFiles = async (v: any) => {
        const container: DataObjectContainer = prepareContainer(v.values);

        container.dataObjectFileList = await dataObjectService.validate(container);
        setDataObjectContainer(container);
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
            await selectFiles(v);
        },
        validations,
        redirectAfterSave: () => {
            openMainArea('DataObjectCreateForm', dataObjectContainer);
        }
    });

    const onChooseFile = (event: React.ChangeEvent<HTMLInputElement>) => {
        const input = event.target;
        if (input.files) {
            const listDataObjectFile: DataObjectFile[] = [];

            Array.from(input.files).forEach(file => {
                const dataObjectFile: DataObjectFile = new DataObjectFile();
                listDataObjectFile.push(dataObjectFile);

                dataObjectFile.originalFileName = file.name;
                const fr = new FileReader();
                fr.onload = (ev) => {
                    dataObjectFile.dataObjectContent = ev.target ? String(ev.target.result) : '';
                };
                fr.onerror = () => {
                    toast.error(i18n.t('message:error:readingFile'));
                };
                fr.readAsText(file, 'UTF-8');
            });

            handleMultipleChange([
                { name: fields.dataObjectFileList.name, value: listDataObjectFile }
            ]);
        }
    };

    const handleLocationTypeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const input = e.target.value;
        if (input.toString() === DataObjectLocationType.MY_COMPUTER.id.toString()) {
            handleMultipleChange([
                { name: fields.dataObjectLocationType.name, value: input },
                { name: fields.location.name, value: '' },
                { name: fields.dataObjectFileList.name, value: [] }
            ]);
        } else if (input.toString() === DataObjectLocationType.FTP.id.toString()) {
            handleMultipleChange([
                { name: fields.dataObjectLocationType.name, value: input },
                { name: fields.dataObjectFileList.name, value: [] }
            ]);
        } else if (input.toString() === DataObjectLocationType.DCA_SERVER.id.toString()) {
            if (!setting || !setting.commonFolder) {
                toast.error(i18n.t('dataObject:message:settingsIsRequired'));
            } else {
                handleMultipleChange([
                    { name: fields.dataObjectLocationType.name, value: input },
                    { name: fields.location.name, value: '' },
                    { name: fields.dataObjectFileList.name, value: [] }
                ]);
            }
        }
    };

    const getDataObjects = (filesChosen: FileData[]): DataObjectFile[] => {
        const dataObjectFiles: DataObjectFile[] = [];
        for (const file of filesChosen) {
            if (!file.isDirectory) {
                const dataObjectFile: DataObjectFile = new DataObjectFile();
                dataObjectFile.originalFileName = file.name;
                dataObjectFile.dataObjectContent = file.content;
                dataObjectFiles.push(dataObjectFile);
            }
        }
        return dataObjectFiles;
    };

    const getLocationNetwork = (path: string): Location => {
        const location: Location = new Location();
        location.locationType = LocationType.NETWORK;
        location.path = path;

        return location;
    };

    return (

        <MediumForm onSubmit={handleSubmit}>
            <FormTitle>{i18n.t('dataObject:titlePlural')}</FormTitle>
            <PanelBreak/>

            <Select
                label={fields.dataObjectType.label}
                name={fields.dataObjectType.name}
                emptySelectText="---"
                options={dataObjectTypes.map((c: DataObjectType) => ({
                    label: i18n.t(BaseEnum.getValue(DataObjectType, c.id).name),
                    value: c.id
                }))}
                value={values[fields.dataObjectType.name]}
                isValid={!errors[fields.dataObjectType.name]}
                validationErrorMessage={errors[fields.dataObjectType.name]}
                onChange={handleChange}
            />

            <br/>

            <OptionSelectionWrapper>
                <OptionWrapper>{fields.dataObjectLocationType.label}</OptionWrapper>
                <OptionCheckboxWrapper>
                    {dataObjectLocationTypes.map((d: DataObjectLocationType) => (
                        <RadioButton
                            label={i18n.t(BaseEnum.getValue(DataObjectLocationType, d.id).name)}
                            name={fields.dataObjectLocationType.name}
                            value={d.id.toString()}
                            checked={d.id.toString() === String(values[fields.dataObjectLocationType.name])}
                            onChange={handleLocationTypeChange}
                        />
                        )
                    )}
                </OptionCheckboxWrapper>
            </OptionSelectionWrapper>

            <br/>

            <div hidden={String(values[fields.dataObjectLocationType.name]).toString() !== DataObjectLocationType.MY_COMPUTER.id}>
                <FileInput
                    label={fields.dataObjectFileList.label}
                    name={fields.dataObjectFileList.name}
                    isValid={!errors[fields.dataObjectFileList.name]}
                    validationErrorMessage={errors[fields.dataObjectFileList.name]}
                    value={getFileName(values[fields.dataObjectFileList.name])}
                    onChange={onChooseFile}
                    onBlur={handleBlur}
                />

                <br/>
            </div>

            <div hidden={String(values[fields.dataObjectLocationType.name]).toString() !== DataObjectLocationType.FTP.id}>
                <Select
                    label={fields.location.label}
                    name={fields.location.name}
                    emptySelectText="---"
                    options={locations.filter(l => l.locationType.id.toString() === LocationType.FTP.id.toString()).map((c: Location) => ({
                        label: c.identifier,
                        value: c.id
                    }))}
                    value={values[fields.location.name]}
                    isValid={!errors[fields.location.name]}
                    validationErrorMessage={errors[fields.location.name]}
                    onChange={handleChange}
                />

                <br/>

                <div hidden={!String(values[fields.location.name]).toString()}>
                    <FileBrowserInput
                        disabled
                        typeToShow={['File']}
                        location={locations.find(l => l.id.toString() === String(values[fields.location.name]))}
                        name={fields.dataObjectFileList.name}
                        value={getFileName(values[fields.dataObjectFileList.name])}
                        handleChange={(filesChosen: FileData[]) => {
                            handleMultipleChange([
                                { name: fields.dataObjectFileList.name, value: getDataObjects(filesChosen) }
                            ]);
                        }}
                        onBlur={handleBlur}
                        label={fields.dataObjectFileList.label}
                        validationErrorMessage={errors[fields.dataObjectFileList.name]}
                    />

                    <br/>
                </div>
            </div>

            <div hidden={String(values[fields.dataObjectLocationType.name]).toString() !== DataObjectLocationType.DCA_SERVER.id}>
                <FileBrowserInput
                    disabled
                    location={getLocationNetwork(setting.commonFolder)}
                    name={fields.dataObjectFileList.name}
                    value={getFileName(values[fields.dataObjectFileList.name])}
                    handleChange={(filesChosen: FileData[]) => {
                        handleMultipleChange([
                            { name: fields.dataObjectFileList.name, value: getDataObjects(filesChosen) }
                        ]);
                    }}
                    onBlur={handleBlur}
                    label={fields.dataObjectFileList.label}
                    validationErrorMessage={errors[fields.dataObjectFileList.name]}
                />

                <br/>
            </div>

            <Actions>
                <Button type="submit" buttonType='primary'>
                    {i18n.t('common:button:select')}
                </Button>
                <Button type="button" buttonType='secondary' onClick={onCancelClick}>
                    {i18n.t('common:button:cancel')}
                </Button>
            </Actions>
        </MediumForm>
    );
};

export default DataObjectContainerForm;