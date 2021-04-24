import { Button, Dialog, TextInput, useDialog } from 'equisoft-design-ui-elements';
import React, { ChangeEvent, useState } from 'react';

import { ChevronsDown, ChevronsUp } from 'react-feather';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { useMainAreaContext } from '../../../components/editor/MainAreaProvider';
import useCustomForm from '../../../components/form/Form';
import { defaultDataObjectService } from '../../../lib/context';
import BaseEnum from '../../../lib/domain/entities/BaseEnum';
import DataObject from '../../../lib/domain/entities/DataObject';
import DataObjectContainer from '../../../lib/domain/entities/DataObjectContainer';
import DataObjectFile from '../../../lib/domain/entities/DataObjectFile';
import DataObjectStatus from '../../../lib/domain/entities/DataObjectStatus';
import DataObjectType from '../../../lib/domain/entities/DataObjectType';
import DataObjectService from '../../../lib/services/DataObjectService';
import { ErrorMessage } from '../../../page/login/styles';
import { Actions, DataObjectTable, FormTitle, LargeForm, PanelBreak } from '../../common/style';
import { useDataObjectFormUtils } from '../common/useDataObjectFormUtils';
import { useDataObjectImportSchema } from './useDataObjectImportSchema';

interface Props {
    data: DataObjectContainer;
    dataObjectService?: DataObjectService;

    onCancelClick(): void;
}

const DataObjectImportForm: React.FC<Props> = ({
                                                 data,
                                                 dataObjectService = defaultDataObjectService,
                                                 onCancelClick
                                             }) => {
    const { i18n } = useTranslation();
    const { fields, validations } = useDataObjectImportSchema();
    const { openMainArea, setShouldAskBeforeLeave } = useMainAreaContext();
    const {
        getContentToDisplay, getStatusDataObjectFile, getStatusColorDataObjectFile, getIconStyle, getFileName,
        getOriginalFileName, getStatusId, sortDataObjectFileListByFileName, sortDataObjectFileListByStatus
    } = useDataObjectFormUtils();

    const [listDataObjectSorted, setListDataObjectSorted] = useState<DataObjectFile[]>([]);
    const [isSortedByFileNameAsc, setIsSortedByFileNameAsc] = useState<boolean>(false);
    const [isSortedByStatusAsc, setIsSortedByStatusAsc] = useState<boolean>(false);
    const [fileToRead, setFileToRead] = useState<DataObjectFile | undefined>();
    const [dialogProps, setDialogProps] = useState({});
    const [show, toggle] = useDialog();

    React.useEffect(() => {
        setShouldAskBeforeLeave(true);

        setListDataObjectSorted(sortDataObjectFileListByFileName(data));
        setIsSortedByFileNameAsc(true);
    }, []);

    const getInitialValues = (): any => {
        const getMapIdentifierInitial = (): Map<string, string> => {
            const mapInitial = new Map<string, string>();
            data.dataObjectListChosenInImportForm.forEach(d => {
                mapInitial.set(getOriginalFileName(d), d.identifier);
            });
            return mapInitial;
        };

        const getMapDescriptionInitial = (): Map<string, string> => {
            const mapInitial = new Map<string, string>();
            data.dataObjectListChosenInImportForm.forEach(d => {
                mapInitial.set(getOriginalFileName(d), d.description);
            });
            return mapInitial;
        };

        return {
            [fields.dataObjectList.name]: data.dataObjectListChosenInImportForm,
            [fields.identifier.name]: getMapIdentifierInitial(),
            [fields.description.name]: getMapDescriptionInitial()
        };
    };

    const save = async (v: any) => {
        const getIdentifiers = (listDataObject: DataObject[]): string => {
            let identifiers = '';

            listDataObject.forEach((dataObject, index) => {
                if (index !== 0) {
                    identifiers = identifiers + ', ';
                }
                identifiers = identifiers + dataObject.identifier;
            });
            return identifiers;
        };

        const prepareToSave = (valueToSave: any): DataObject[] => {
            const dataObjectsToSave: DataObject[] = [];
            const listDataObjectChosen: DataObject[] = valueToSave[fields.dataObjectList.name];

            const listIdentifier: Map<string, string> = valueToSave[fields.identifier.name];
            const listDescription: Map<string, string> = valueToSave[fields.description.name];
            listDataObjectChosen.forEach(d => {
                d.identifier = listIdentifier.get(getOriginalFileName(d)) || '';
                d.description = listDescription.get(getOriginalFileName(d)) || '';
                dataObjectsToSave.push(d);
            });

            return dataObjectsToSave;
        };

        let dataObjectToSave: DataObject[] = prepareToSave(v.values);
        data.dataObjectListChosenInImportForm = dataObjectToSave;

        dataObjectToSave = await dataObjectService.import(dataObjectToSave);
        data.dataObjectListImported = dataObjectToSave;

        toast.success(i18n.t('message:success:save', { what: getIdentifiers(dataObjectToSave) }));
        return;
    };

    const {
        handleSubmit,
        handleMultipleChange,
        values,
        errors
    } = useCustomForm({
        initialValues: getInitialValues(),
        onSubmit: async (v: any) => {
            await save(v);
        },
        validations,
        redirectAfterSave: () => {
            openMainArea('DataObjectResultForm', data);
        }
    });

    const getDataObject = (list: DataObject[], fileName: string): DataObject | undefined => {
        return list.find(o => getOriginalFileName(o).toString() === fileName.toString());
    };

    const isDataObjectValid = (dataObjectFile: DataObjectFile): boolean => {
        return !!dataObjectFile.status && dataObjectFile.status.id.toString() === DataObjectStatus.SUCCESS.id.toString()
            && !!data.dataObjectFileListChosenInCreateForm.find(d => d.originalFileName.toString() === dataObjectFile.originalFileName.toString());
    };

    const getDataObjectsValid = (): DataObjectFile[] => {
        return data.dataObjectFileList.filter(isDataObjectValid);
    };

    const checkBoxChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        const input = e.target.value;
        const listDataObjectChosen: DataObject[] = values[fields.dataObjectList.name] as DataObject[];

        let listDataObjectClone: DataObject[] = [...listDataObjectChosen];
        const listIdentifierClone: Map<string, string> = new Map(values[fields.identifier.name]);
        const listDescriptionClone: Map<string, string> = new Map(values[fields.description.name]);

        if (isChecked(input)) {
            listDataObjectClone = listDataObjectChosen.filter(o => getOriginalFileName(o).toString() !== input.toString());
            listIdentifierClone.delete(input);
            listDescriptionClone.delete(input);
        } else {
            const dataObjectChosen = getDataObject(data.dataObjectListChosenInImportForm, input);
            if (dataObjectChosen) {
                listDataObjectClone.push(dataObjectChosen);
                if (!listIdentifierClone.get(getOriginalFileName(dataObjectChosen))) {
                    listIdentifierClone.set(getOriginalFileName(dataObjectChosen), '');
                }
                if (!listDescriptionClone.get(getOriginalFileName(dataObjectChosen))) {
                    listDescriptionClone.set(getOriginalFileName(dataObjectChosen), '');
                }
            }
        }
        handleMultipleChange([
            { name: fields.dataObjectList.name, value: listDataObjectClone },
            { name: fields.identifier.name, value: listIdentifierClone },
            { name: fields.description.name, value: listDescriptionClone }
        ]);
    };

    const selectAllHandler = () => {
        const list: DataObject[] = [];
        const listIdentifierClone: Map<string, string> = new Map(values[fields.identifier.name]);
        const listDescriptionClone: Map<string, string> = new Map(values[fields.description.name]);

        if (!isCheckedAll()) {
            getDataObjectsValid().forEach(o => {
                const newDataObject: DataObject = new DataObject(data.system);
                newDataObject.type = data.type;
                list.push(newDataObject);
                if (!listIdentifierClone.get(o.originalFileName)) {
                    listIdentifierClone.set(o.originalFileName, '');
                }
                if (!listDescriptionClone.get(o.originalFileName)) {
                    listDescriptionClone.set(o.originalFileName, '');
                }
            });
        } else {
            listDescriptionClone.clear();
            listIdentifierClone.clear();
        }

        handleMultipleChange([
            { name: fields.dataObjectList.name, value: list },
            { name: fields.identifier.name, value: listIdentifierClone },
            { name: fields.description.name, value: listDescriptionClone }
        ]);
    };

    const isChecked = (fileName: string): boolean => {
        const listDataObjectChosen: DataObject[] = values[fields.dataObjectList.name] as DataObject[];
        return !!getDataObject(listDataObjectChosen, fileName);
    };

    const isCheckedAll = (): boolean => {
        const listDataObjectChosen: DataObject[] = values[fields.dataObjectList.name] as DataObject[];
        return listDataObjectChosen.length === getDataObjectsValid().length && listDataObjectChosen.length !== 0;
    };

    const isFileValidAndChosen = (fileName: string): boolean => {
        const dataObject = getDataObject(data.dataObjectListChosenInImportForm, fileName);
        const dataObjectChosen = getDataObject(values[fields.dataObjectList.name] as DataObject[], fileName);
        if (dataObject && dataObjectChosen) {
            return getStatusId(dataObject).toString() === DataObjectStatus.SUCCESS.id.toString();
        }
        return false;
    };

    const handleIdentifierChange = (event: ChangeEvent<HTMLInputElement>, fileName: string): void => {
        const mapIdentifier: Map<string, string> = values[fields.identifier.name] as Map<string, string>;
        const mapClone = new Map(mapIdentifier);
        mapClone.delete(fileName);
        mapClone.set(fileName, event.target.value);

        handleMultipleChange([{ name: fields.identifier.name, value: mapClone }]);
    };

    const handleDescriptionChange = (event: ChangeEvent<HTMLInputElement>, fileName: string): void => {
        const mapDescription: Map<string, string> = values[fields.description.name] as Map<string, string>;
        const mapClone = new Map(mapDescription);
        mapClone.delete(fileName);
        mapClone.set(fileName, event.target.value);

        handleMultipleChange([{ name: fields.description.name, value: mapClone }]);
    };

    const getInputField = (fieldName: string, fileName: string): string => {
        const input = (values[fieldName] as Map<string, string>).get(fileName);
        return input || '';
    };

    const getErrorField = (fieldName: string, fileName: string): string => {
        const mapError = (errors[fieldName] as Map<string, string>);
        if (mapError) {
            return mapError.get(fileName) || '';
        }
        return '';
    };

    const getDialogProps = () => {
        const closeDialog = () => {
            setDialogProps({});
            setFileToRead(undefined);
            toggle();
        };

        const baseProps = {
            show,
            title: fileToRead ? fileToRead.originalFileName : '',
            element:
                <>
                    <pre style={{ height: 400, overflow: 'auto', border: 'solid' }}>
                        {getContentToDisplay(fileToRead ? fileToRead.dataObjectContent : '')}
                    </pre>
                </>,
            onClose: closeDialog

        };
        return { ...baseProps, ...dialogProps };
    };

    const getDataObjectSaved = (dataObjectFile: DataObjectFile): DataObject | undefined => {
        return data.dataObjectListChosenInImportForm.find(d => getOriginalFileName(d).toString() === dataObjectFile.originalFileName.toString());
    };

    return (

        <LargeForm onSubmit={handleSubmit}>
            <FormTitle>{i18n.t('dataObject:titlePlural')}</FormTitle>
            <PanelBreak/>

            <TextInput
                disabled
                label={fields.selectedSystem.label}
                name={fields.selectedSystem.name}
                value={data.system.identifier}
            />

            <TextInput
                disabled
                label={fields.selectedFileType.label}
                name={fields.selectedFileType.name}
                value={String(i18n.t(BaseEnum.getValue(DataObjectType, data.type ? data.type.id : '').name))}
            />

            <TextInput
                disabled
                label={fields.selectedFiles.label}
                name={fields.selectedFiles.name}
                value={getFileName(data.dataObjectFileList)}
            />

            <br/>

            {errors[fields.dataObjectList.name] && <ErrorMessage>{errors[fields.dataObjectList.name]}</ErrorMessage>}

            {listDataObjectSorted.length > 0 && (
                <>
                    <DataObjectTable>
                        <thead>
                        <tr>
                            <th>
                                <input
                                    style={{ cursor: 'pointer' }}
                                    type={'checkbox'}
                                    onChange={selectAllHandler}
                                    checked={isCheckedAll()}
                                    disabled={listDataObjectSorted.every(d => !isDataObjectValid(d))}
                                />
                                &nbsp;
                                <label style={{ cursor: 'pointer' }} onClick={() => {
                                    const newList: DataObjectFile[] = [];
                                    Array.prototype.push.apply(newList, isSortedByFileNameAsc ? sortDataObjectFileListByFileName(data).reverse() : sortDataObjectFileListByFileName(data));
                                    setListDataObjectSorted(newList);
                                    setIsSortedByFileNameAsc(!isSortedByFileNameAsc);
                                }}>
                                    {i18n.t('dataObject:file')}
                                    {isSortedByFileNameAsc ? <ChevronsDown style={getIconStyle()}/> : <ChevronsUp style={getIconStyle()}/>}
                                </label>
                            </th>
                            <th>
                                <label style={{ cursor: 'pointer' }} onClick={() => {
                                    const newList: DataObjectFile[] = [];
                                    Array.prototype.push.apply(newList, isSortedByStatusAsc ? sortDataObjectFileListByStatus(data).reverse() : sortDataObjectFileListByStatus(data));
                                    setListDataObjectSorted(newList);
                                    setIsSortedByStatusAsc(!isSortedByStatusAsc);
                                }}>
                                    {i18n.t('dataObject:result')}
                                    {isSortedByStatusAsc ? <ChevronsDown style={getIconStyle()}/> : <ChevronsUp style={getIconStyle()}/>}
                                </label>
                            </th>
                            <th>{i18n.t('dataObject:identifier')}</th>
                            <th>{i18n.t('dataObject:description')}</th>
                        </tr>
                        </thead>
                        <tbody>
                        {listDataObjectSorted.map((item) => (
                            <tr key={item.originalFileName}>
                                <td>
                                    <input
                                        style={{ cursor: 'pointer' }}
                                        type={'checkbox'}
                                        value={item.originalFileName}
                                        onChange={checkBoxChangeHandler}
                                        checked={isChecked(item.originalFileName)}
                                        disabled={!isDataObjectValid(item)}
                                    />
                                    &nbsp;

                                    <a href={'#'}
                                       onClick={e => {
                                           e.preventDefault();
                                           toggle();
                                           setFileToRead(item);
                                           return false;
                                       }}>
                                        {item.originalFileName}
                                    </a>

                                </td>
                                <td style={{ color: getStatusColorDataObjectFile(item, !!getDataObjectSaved(item)) }}>{getStatusDataObjectFile(item, !!getDataObjectSaved(item))}</td>
                                <td>
                                    <TextInput
                                        disabled={!isFileValidAndChosen(item.originalFileName)}
                                        name={fields.identifier.name + item.originalFileName}
                                        value={getInputField(fields.identifier.name, item.originalFileName)}
                                        isValid={!getErrorField(fields.identifier.name, item.originalFileName)}
                                        validationErrorMessage={getErrorField(fields.identifier.name, item.originalFileName)}
                                        maxLength={fields.identifier.maxSize}
                                        onChange={e => handleIdentifierChange(e, item.originalFileName)}
                                    />
                                </td>
                                <td>
                                    <TextInput
                                        disabled={!isFileValidAndChosen(item.originalFileName)}
                                        name={fields.description.name + item.originalFileName}
                                        value={getInputField(fields.description.name, item.originalFileName)}
                                        isValid={!getErrorField(fields.description.name, item.originalFileName)}
                                        validationErrorMessage={getErrorField(fields.description.name, item.originalFileName)}
                                        maxLength={fields.description.maxSize}
                                        onChange={e => handleDescriptionChange(e, item.originalFileName)}
                                    />
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </DataObjectTable>
                </>
            )}

            <br/>

            <Actions>
                <Button type="button" buttonType='secondary' onClick={() => {
                    openMainArea('DataObjectContainerForm', data, async () => {
                        data.dataObjectFileListChosenInCreateForm = [];
                        data.dataObjectListChosenInImportForm = [];
                    });
                }}>
                    {i18n.t('dataObject:selectFiles')}
                </Button>
                <Button type="button" buttonType='secondary' onClick={() => {
                    openMainArea('DataObjectCreateForm', data, async () => {
                        data.dataObjectListChosenInImportForm = [];
                    });
                }}>
                    {i18n.t('dataObject:createDataObjects')}
                </Button>
                <Button type="submit" buttonType='primary'>
                    {i18n.t('dataObject:importSelected')}
                </Button>
                <Button type="button" buttonType='secondary' onClick={onCancelClick}>
                    {i18n.t('common:button:cancel')}
                </Button>
            </Actions>
            <Dialog {...getDialogProps()} />
        </LargeForm>
    );
};

export default DataObjectImportForm;