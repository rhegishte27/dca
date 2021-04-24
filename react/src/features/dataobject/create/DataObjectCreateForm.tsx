import { Button, Dialog, Label, TextInput, useDialog } from 'equisoft-design-ui-elements';
import React, { ChangeEvent, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useMainAreaContext } from '../../../components/editor/MainAreaProvider';
import useCustomForm from '../../../components/form/Form';
import { TextInputWithButtonWrapper } from '../../../components/general/input/fileexplorerinput/style';
import { defaultDataObjectService } from '../../../lib/context';
import BaseEnum from '../../../lib/domain/entities/BaseEnum';
import DataObjectContainer from '../../../lib/domain/entities/DataObjectContainer';
import DataObjectFile from '../../../lib/domain/entities/DataObjectFile';
import DataObjectStatus from '../../../lib/domain/entities/DataObjectStatus';
import DataObjectType from '../../../lib/domain/entities/DataObjectType';
import DataObjectService from '../../../lib/services/DataObjectService';
import { ErrorMessage } from '../../../page/login/styles';
import { sortList } from '../../common/listUtil';
import { Actions, DataObjectTable, FormTitle, MediumForm, PanelBreak } from '../../common/style';
import { useDataObjectFormUtils } from '../common/useDataObjectFormUtils';
import { useDataObjectCreateSchema } from './useDataObjectCreateSchema';

interface Props {
    data: DataObjectContainer;
    dataObjectService?: DataObjectService;

    onCancelClick(): void;
}

const DataObjectCreateForm: React.FC<Props> = ({
                                                   data,
                                                   dataObjectService = defaultDataObjectService,
                                                   onCancelClick
                                               }) => {
    const { i18n } = useTranslation();
    const { fields, validations } = useDataObjectCreateSchema();
    const { openMainArea, setShouldAskBeforeLeave } = useMainAreaContext();
    const { getContentToDisplay, getFileName } = useDataObjectFormUtils();

    const [fileToRead, setFileToRead] = useState<DataObjectFile | undefined>();
    const [dialogProps, setDialogProps] = useState({});
    const [show, toggle] = useDialog();

    React.useEffect(() => {
        setShouldAskBeforeLeave(true);
    }, []);

    const getInitialValues = (): any => {
        return {
            [fields.dataObjectFileList.name]: data.dataObjectFileListChosenInCreateForm
        };
    };

    const save = async (v: any) => {
        const dataObjectFileToCreate: DataObjectFile[] = v.values[fields.dataObjectFileList.name];
        data.dataObjectFileListChosenInCreateForm = dataObjectFileToCreate;

        const dataObjectCreated = await dataObjectService.create(data);
        data.dataObjectListChosenInImportForm = dataObjectCreated;
        return;
    };

    const {
        handleSubmit,
        handleChange,
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
            openMainArea('DataObjectImportForm', data);
        }
    });

    const getDataObjectFile = (list: DataObjectFile[], fileName: string): DataObjectFile | undefined => {
        return list.find(o => o.originalFileName.toString() === fileName.toString());
    };

    const isDataObjectFileValid = (dataObjectFile: DataObjectFile): boolean => {
        return !!dataObjectFile.status && dataObjectFile.status.id.toString() === DataObjectStatus.SUCCESS.id.toString();
    };

    const getDataObjectFileValid = (): DataObjectFile[] => {
        return data.dataObjectFileList.filter(isDataObjectFileValid);
    };

    const checkBoxChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        const input = e.target.value;
        const listDataObjectChosen: DataObjectFile[] = values[fields.dataObjectFileList.name] as DataObjectFile[];

        let listDataObjectClone: DataObjectFile[] = [...listDataObjectChosen];

        if (isChecked(input)) {
            listDataObjectClone = listDataObjectChosen.filter(o => o.originalFileName.toString() !== input.toString());
        } else {
            const dataObjectChosen = getDataObjectFile(data.dataObjectFileList, input);
            if (dataObjectChosen) {
                listDataObjectClone.push(dataObjectChosen);
            }
        }
        handleMultipleChange([
            { name: fields.dataObjectFileList.name, value: listDataObjectClone }
        ]);
    };

    const selectAllHandler = () => {
        const list: DataObjectFile[] = [];

        if (!isCheckedAll()) {
            getDataObjectFileValid().forEach(o => {
                list.push(o);
            });
        }

        handleMultipleChange([
            { name: fields.dataObjectFileList.name, value: list }
        ]);
    };

    const isChecked = (fileName: string): boolean => {
        const listDataObjectChosen: DataObjectFile[] = values[fields.dataObjectFileList.name] as DataObjectFile[];
        return !!getDataObjectFile(listDataObjectChosen, fileName);
    };

    const isCheckedAll = (): boolean => {
        const listDataObjectChosen: DataObjectFile[] = values[fields.dataObjectFileList.name] as DataObjectFile[];
        return listDataObjectChosen.length === getDataObjectFileValid().length && listDataObjectChosen.length !== 0;
    };

    const getFileContentDescription = (dataObjectFile: DataObjectFile): string => {
        return isDataObjectFileValid(dataObjectFile) ? i18n.t('dataObject:message:fileMatchSelectedType') : i18n.t('dataObject:message:fileNotMatchSelectedType');
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

    const selectFilesMatchingExpression = (): void => {
        const expression: string = values[fields.selectFilesMatchingExpression.name];
        const listFilesMatched: DataObjectFile[] = [];
        if (expression) {
            data.dataObjectFileList.forEach(d => {
                const regex = new RegExp(expression);
                if (regex.test(d.originalFileName) && isDataObjectFileValid(d)) {
                    listFilesMatched.push(d);
                }
            });

            handleMultipleChange([
                { name: fields.dataObjectFileList.name, value: listFilesMatched }
            ]);
        }
    };

    return (

        <MediumForm onSubmit={handleSubmit}>
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

            <div>
                <TextInputWithButtonWrapper>
                    <Label>{fields.selectFilesMatchingExpression.label}</Label>
                    <span>&nbsp;</span>
                    <TextInput
                        name={fields.selectFilesMatchingExpression.name}
                        value={values[fields.selectFilesMatchingExpression.name]}
                        onChange={handleChange}
                    />
                    <span>&nbsp;</span>
                    <Button type="button" buttonType='secondary' onClick={selectFilesMatchingExpression}>
                        {i18n.t('common:button:select')}
                    </Button>
                </TextInputWithButtonWrapper>
            </div>

            <br/>

            {errors[fields.dataObjectFileList.name] && <ErrorMessage>{errors[fields.dataObjectFileList.name]}</ErrorMessage>}

            {data.dataObjectFileList.length > 0 && (
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
                                    disabled={data.dataObjectFileList.every(d => !isDataObjectFileValid(d))}
                                />
                                &nbsp;
                                <label style={{ cursor: 'pointer' }}>
                                    {i18n.t('dataObject:file')}
                                </label>
                            </th>
                            <th>{i18n.t('dataObject:fileContents')}</th>
                        </tr>
                        </thead>
                        <tbody>
                        {sortList(data.dataObjectFileList, (d: DataObjectFile) => d.originalFileName).map((item) => (
                            <tr key={item.originalFileName}>
                                <td>
                                    <input
                                        style={{ cursor: 'pointer' }}
                                        type={'checkbox'}
                                        value={item.originalFileName}
                                        onChange={checkBoxChangeHandler}
                                        checked={isChecked(item.originalFileName)}
                                        disabled={!isDataObjectFileValid(item)}
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
                                <td>{getFileContentDescription(item)}</td>
                            </tr>
                        ))}
                        </tbody>
                    </DataObjectTable>
                </>
            )}

            <br/>

            <Actions>
                <Button type="button" buttonType='secondary' onClick={() => openMainArea('DataObjectContainerForm', data, async () => {
                    data.dataObjectFileListChosenInCreateForm = [];
                })}>
                    {i18n.t('dataObject:selectFiles')}
                </Button>
                <Button type="submit" buttonType='primary'>
                    {i18n.t('dataObject:createDataObjects')}
                </Button>
                <Button type="button" buttonType='secondary' onClick={onCancelClick}>
                    {i18n.t('common:button:cancel')}
                </Button>
            </Actions>
            <Dialog {...getDialogProps()} />
        </MediumForm>
    );
};

export default DataObjectCreateForm;