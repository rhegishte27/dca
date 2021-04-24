import { ChonkyActions, ChonkyFileActionData, FileArray, FileHelper } from 'chonky';
import { OpenFilesPayload } from 'chonky/lib/types/action-payloads.types';
import { useCallback, useMemo } from 'react';
import FileData from '../../../../lib/domain/entities/FileData';
import useFileAction from './useFileAction';
import useFileDataArray from './useFileDataArray';

const useActionHandler = () => {
    const {findByPath, pushToFileArray, unShiftToFileArray} = useFileDataArray();
    const {CreateFolderAction, DeleteFileAction} = useFileAction();

    const useFiles = (listFiles: FileData[], folderPath: string): FileArray => {
        return useMemo(() => {
            const currentFolder = findByPath(listFiles, folderPath);
            const lstFiles: FileArray = [];
            if (currentFolder) {
                currentFolder.childrenPathList.forEach(childPath => {
                    const child = findByPath(listFiles, childPath);
                    pushToFileArray(lstFiles, child);
                });
            }
            return lstFiles;
        }, [folderPath, listFiles]);
    };

    const useFolderChain = (listFiles: FileData[], folderPath: string): FileArray => {
        return useMemo(() => {
            const currentFolder = findByPath(listFiles, folderPath);
            const chain: FileArray = [];

            pushToFileArray(chain, currentFolder);

            if (currentFolder) {
                let parentId = currentFolder.parentPath;
                while (parentId) {
                    const parentFile = findByPath(listFiles, parentId);
                    if (parentFile) {
                        unShiftToFileArray(chain, parentFile);
                        parentId = parentFile.parentPath;
                    } else {
                        parentId = null;
                    }
                }
            }
            return chain;
        }, [folderPath, listFiles]);
    };

    const useFileActionHandler = (listFiles: FileData[],
                                  setFolderPath: (folderPath: string) => void,
                                  toggleInputFolderName: () => void,
                                  toggleConfirmationDelete: (listFilesDelete: FileData[]) => void) => {
        return useCallback((data: ChonkyFileActionData) => {
                if (data.id === ChonkyActions.OpenFiles.id) {
                    handleOpenFilesAction(data, setFolderPath);
                }

                if (data.id === CreateFolderAction.id) {
                    handleCreateFolderAction(toggleInputFolderName);
                }

                if (data.id === DeleteFileAction.id) {
                    handleDeleteFileAction(data, listFiles, toggleConfirmationDelete);
                }
            }, [setFolderPath, listFiles]
        );
    };

    const handleOpenFilesAction = (data: ChonkyFileActionData,
                                   setFolderPath: (folderPath: string) => void) => {
        const {targetFile, files} = (data.payload as OpenFilesPayload);
        const fileToOpen = targetFile ? targetFile : files[0];
        if (fileToOpen && FileHelper.isDirectory(fileToOpen)) {
            setFolderPath(fileToOpen.id);
            return;
        }
    };

    const handleCreateFolderAction = (toggleInputFolderName: () => void) => {
        toggleInputFolderName();
    };

    const handleDeleteFileAction = (data: ChonkyFileActionData,
                                    listFiles: FileData[],
                                    toggleConfirmationDelete: (listFileDelete: FileData[]) => void) => {
        const selectedFiles = data.state.selectedFiles;
        const lstDelete: FileData[] = [];
        if (selectedFiles) {
            selectedFiles.forEach((f: any) => {
                const file = findByPath(listFiles, f.id);
                if (file) {
                    lstDelete.push(file);
                }
            });
            toggleConfirmationDelete(lstDelete);
        }
    };

    return {useFileActionHandler, useFolderChain, useFiles};
};

export default useActionHandler;