import { Button, Dialog, Label, TextInput, useDialog } from 'equisoft-design-ui-elements';
import { TextInputProps } from 'equisoft-design-ui-elements/dist/components/forms/inputs/textInput';
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { ErrorMessage } from '../../../../features/common/style';
import { defaultDirectoryService, defaultLocationService } from '../../../../lib/context';
import FileData from '../../../../lib/domain/entities/FileData';
import Location from '../../../../lib/domain/entities/Location';
import LocationType from '../../../../lib/domain/entities/LocationType';
import DirectoryService from '../../../../lib/services/DirectoryService';
import LocationService from '../../../../lib/services/LocationService';
import FileSystemBrowser from '../../fileexplorer/FileSystemBrowser';
import { TextInputWithButtonWrapper } from './style';

type FileType =
    | 'Directory'
    | 'File';

interface FileExplorerInputProps extends TextInputProps {
    location?: Location
    isBrowseButtonDisable?: boolean;
    typeToShow?: FileType[];
    isCreateAndDeleteFolderAllow?: boolean;
    label?: string;
    validationErrorMessage?: string
    directoryService?: DirectoryService;
    locationService?: LocationService;

    handleChange(filesChosen: FileData[]): void;
}

const FileBrowserInput: React.FC<FileExplorerInputProps> = ({
                                                                location,
                                                                label,
                                                                validationErrorMessage,
                                                                isBrowseButtonDisable,
                                                                typeToShow = ['Directory', 'File'],
                                                                isCreateAndDeleteFolderAllow = false,
                                                                directoryService = defaultDirectoryService,
                                                                locationService = defaultLocationService,
                                                                handleChange,
                                                                ...props
                                                            }) => {
    const {i18n} = useTranslation('common');
    const [listFileInitial, setListFileInitial] = useState<FileData[]>([]);
    const [dialogProps, setDialogProps] = useState({});
    const [show, toggle] = useDialog();

    const closeDialog = () => {
        setDialogProps({});
        toggle();
    };

    const getDialogProps = () => {
        const baseProps = {
            show,
            title: '',
            onClose: closeDialog,
            element:
                <FileSystemBrowser
                    directoryService={directoryService}
                    listFilesInitial={listFileInitial}
                    isCreateAndDeleteFolderAllow={isCreateAndDeleteFolderAllow}
                    closeDialog={closeDialog}
                    handleChange={handleChange}
                />,
            confirmMessage: i18n.t('common:button:select'),
            cancelMessage: i18n.t('common:button:cancel')
        };
        return {...baseProps, ...dialogProps};
    };

    const handleBrowserClick = (): void => {
        const handleClick = (listFileData: FileData[]): void => {
            const listInitial: FileData[] = [];

            const root = listFileData.find(f => !f.parentPath);
            if (root) {
                listInitial.push(root);
            } else {
                throw Error('Root is required');
            }

            for (const fileType of typeToShow) {
                if (fileType === 'Directory') {
                    listInitial.push(...listFileData.filter(file => file.parentPath && file.isDirectory));
                }
                if (fileType === 'File') {
                    listInitial.push(...listFileData.filter(file => file.parentPath && !file.isDirectory));
                }
            }

            setListFileInitial(listInitial);
            toggle();
        };

        if (location) {
            if (location.locationType.id.toString() === LocationType.NETWORK.id.toString()) {
                directoryService.getDirectories(location.path).then(listFileData => {
                    handleClick(listFileData);
                });
            } else if (location.locationType.id.toString() === LocationType.FTP.id.toString()) {
                locationService.getFiles(location.id).then(listFileData => {
                    handleClick(listFileData);
                });
            }
        }
    };

    return (
        <>
            {label && <Label>{label}</Label>}
            {
                validationErrorMessage &&
				<div>
					<ErrorMessage>{validationErrorMessage}</ErrorMessage>
				</div>
            }
            <TextInputWithButtonWrapper>
                <TextInput
                    {...props}
                />
                <span>&nbsp;</span>
                <Button
                    disabled={isBrowseButtonDisable}
                    onClick={handleBrowserClick}
                    type={'button'}
                    buttonType={'secondary'}
                >
                    {i18n.t('common:button:browse')}
                </Button>
            </TextInputWithButtonWrapper>
            <Dialog {...getDialogProps()} />
        </>
    );

};

export default FileBrowserInput;