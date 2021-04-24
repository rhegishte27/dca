import { FileArray } from 'chonky';
import FileData from '../../../../lib/domain/entities/FileData';

const useFileDataArray = () => {

    const findRoot = (lst: FileData[]): FileData | null => {
        const root = lst.find(file => file.parentPath === null);
        return root ? root : null;
    };

    const findByPath = (lst: FileData[], path: string): FileData | null => {
        const fileData = lst.find(file => file.path === path);
        return fileData ? fileData : null;
    };

    const toFileData = (fileData: FileData): any => {
        return {
            id: fileData.path,
            name: fileData.name,
            isDir: fileData.isDirectory,
            childrenIds: fileData.childrenPathList,
            parentId: fileData.parentPath,
            childrenCount: fileData.childrenPathList.length
        };
    };

    const pushToFileArray = (array: FileArray, fileData: FileData | null): void => {
        if (fileData) {
            array.push(toFileData(fileData));
        }
    };

    const unShiftToFileArray = (array: FileArray, fileData: FileData | null): void => {
        if (fileData) {
            array.unshift(toFileData(fileData));
        }
    };

    const addNewFolder = (lst: FileData[], newFolder: FileData): FileData[] => {
        const newLst = [...lst];
        newLst.push(newFolder);
        if (newFolder.parentPath) {
            const parent = findByPath(newLst, newFolder.parentPath);
            if (parent) {
                parent.childrenPathList = [...parent.childrenPathList, newFolder.path];
            }
        }
        return newLst;
    };

    const deleteFile = (lst: FileData[], listFileDelete: FileData[]): FileData[] => {
        const lstPathDeleted: string[] = [];

        listFileDelete.forEach((fileDelete: FileData) => {
            lstPathDeleted.push(fileDelete.path);
            lstPathDeleted.push.apply(lstPathDeleted, getLstPathChildrenOfAllLevel(lst, fileDelete));
        });


        let newLst = [...lst];
        newLst = newLst.filter(fileData => {
            for (const pathDelete of lstPathDeleted) {
                if (fileData.path === pathDelete) {
                    return false;
                }
            }
            return true;
        });

        newLst.forEach(file => {
            file.childrenPathList.filter(path => {
                for (const pathDelete of lstPathDeleted) {
                    if (path === pathDelete) {
                        return false;
                    }
                }
                return true;
            });
        });

        return newLst;
    };

    const getLstPathChildrenOfAllLevel = (lst: FileData[], fileData: FileData): string[] => {
        const lstPath: string[] = [];
        lstPath.push.apply(lstPath, fileData.childrenPathList);

        for (const path of fileData.childrenPathList) {
            const children = findByPath(lst, path);
            if (children) {
                lstPath.push.apply(lstPath, getLstPathChildrenOfAllLevel(lst, children));
            }
        }

        return lstPath;
    };

    const isFolderNameDuplicated = (lst: FileData[], currentPath: string, folderName: string): boolean => {
        const currentFolder = findByPath(lst, currentPath);
        if (currentFolder) {
            for (const path of currentFolder.childrenPathList) {
                const child = findByPath(lst, path);
                if (child && child.name === folderName) {
                    return true;
                }
            }
        }
        return false;
    };

    const getListFileName = (listFile: FileData[]): string => {
        let str: string = '';
        listFile.forEach((d, index) => {
            if (index !== 0) {
                str = str + ', ';
            }
            str = str + d.name;

        });
        return str;
    };

    return {findRoot, findByPath, pushToFileArray, unShiftToFileArray, addNewFolder, isFolderNameDuplicated, deleteFile, getListFileName};
};

export default useFileDataArray;
