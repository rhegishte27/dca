import { ChonkyActions, FileBrowser, FileList, FileNavbar, FileToolbar } from 'chonky';
import { ChonkyIconFA } from 'chonky-icon-fontawesome/lib';
import { FileBrowserHandle } from 'chonky/lib/types/file-browser.types';
import { Button, useDialog } from 'equisoft-design-ui-elements';
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import FileData from '../../../lib/domain/entities/FileData';
import DirectoryService from '../../../lib/services/DirectoryService';
import { useConfirmationDialogContext } from '../dialog/confirmation/ConfirmationDialogProvider';
import DialogInputFolderName from './dialog/DialogInputFolderName';
import { Buttons } from './style';
import useActionHandler from './utils/useActionHandler';
import useFileAction from './utils/useFileAction';
import useFileDataArray from './utils/useFileDataArray';

interface FileExplorerProps {
    listFilesInitial: FileData[];
    isCreateAndDeleteFolderAllow: boolean;
    directoryService: DirectoryService;

    handleChange(filesChosen: FileData[]): void;
    closeDialog(): void;
}

const FileSystemBrowser: React.FC<FileExplorerProps> = ({ listFilesInitial, isCreateAndDeleteFolderAllow, handleChange, closeDialog, directoryService }) => {
    const {i18n} = useTranslation('common');
    const [listFiles, setListFiles] = useState<FileData[]>([]);
    const [currentFolderPath, setCurrentFolderPath] = useState('');

    const { openConfirmationDialogDelete } = useConfirmationDialogContext();
    const [showInputFolderName, toggleInputFolderName] = useDialog();

    const {CreateFolderAction, DeleteFileAction} = useFileAction();
    const {useFiles, useFolderChain, useFileActionHandler} = useActionHandler();
    const { findRoot, findByPath, addNewFolder, isFolderNameDuplicated, deleteFile, getListFileName } = useFileDataArray();

    const filesData = useFiles(listFiles, currentFolderPath);
    const folderChain = useFolderChain(listFiles, currentFolderPath);

    const toggleConfirmationDelete = (listFilesDelete: FileData[]) => {
        openConfirmationDialogDelete(() => handleConfirmDeleteFiles(listFilesDelete), getListFileName(listFilesDelete));
    };

    const handleFileAction =
        useFileActionHandler(listFiles, setCurrentFolderPath, toggleInputFolderName, toggleConfirmationDelete);

    const fileBrowserRef = React.useRef<FileBrowserHandle | null>(null);

    React.useEffect(() => {
        setListFiles(listFilesInitial);
        const root = findRoot(listFilesInitial);
        if (root) {
            setCurrentFolderPath(root.path);
        }
    }, []);

    const handleSelect = () => {
        closeDialog();

        const filesSelected: FileData[] = [];
        if (fileBrowserRef.current && fileBrowserRef.current.getFileSelection().size > 0) {
            for (const path of fileBrowserRef.current.getFileSelection()) {
                const fileSelected = findByPath(listFiles, path);
                if (fileSelected) {
                    filesSelected.push(fileSelected);
                }
            }
        } else {
            const fileSelected = findByPath(listFiles, currentFolderPath);
            if (fileSelected) {
                filesSelected.push(fileSelected);
            }
        }
        handleChange(filesSelected);
    };

    const handleConfirmDeleteFiles = (listFilesDelete: FileData[]) => {
        listFilesDelete.forEach(async fileDelete => {
            await directoryService.deleteFile(fileDelete.path);
        });
        setListFiles(deleteFile(listFiles, listFilesDelete));
    };

    const handleConfirmCreateFolder = (folderName: string) => {
        directoryService.createDirectory(currentFolderPath, folderName).then((o) => {
            setListFiles(addNewFolder(listFiles, o));
        });
    };

    return (
        <>
            <div style={{height: 400}}>
                <FileBrowser
                    ref={fileBrowserRef}
                    files={filesData}
                    folderChain={folderChain}
                    onFileAction={handleFileAction}
                    fileActions={isCreateAndDeleteFolderAllow ? [
                        ChonkyActions.EnableListView,
                        ChonkyActions.EnableGridView,
                        CreateFolderAction,
                        DeleteFileAction
                    ] : [
                        ChonkyActions.EnableListView,
                        ChonkyActions.EnableGridView
                    ]}
                    disableDefaultFileActions
                    disableDragAndDrop
                    defaultFileViewActionId={ChonkyActions.EnableListView.id}
                    iconComponent={ChonkyIconFA}
                >
                    <FileNavbar/>
                    <FileToolbar/>
                    <FileList/>
                </FileBrowser>
            </div>

            <br/>

            <Buttons>
                <Button buttonType='secondary' onClick={closeDialog}>
                    {i18n.t('common:button:cancel')}
                </Button>

                <Button type="submit" buttonType='primary' onClick={handleSelect}>
                    {i18n.t('common:button:select')}
                </Button>
            </Buttons>

            <div onClick={e => e.stopPropagation()}>
                <DialogInputFolderName
                    show={showInputFolderName}
                    toggle={toggleInputFolderName}
                    isFolderNameDuplicate={(folderName) => isFolderNameDuplicated(listFiles, currentFolderPath, folderName)}
                    onConfirm={handleConfirmCreateFolder}
                />
            </div>
        </>
    );
};

export default FileSystemBrowser;