import { ChonkyIconName, defineFileAction } from 'chonky';
import { useTranslation } from 'react-i18next';

const useFileAction = () => {
    const {i18n} = useTranslation('common');

    const CreateFolderAction = defineFileAction({
        id: 'create_folder_action',
        button: {
            name: i18n.t('common:button:createFolder'),
            toolbar: true,
            icon: ChonkyIconName.folderCreate
        }
    });

    const DeleteFileAction = defineFileAction({
        id: 'delete_action',
        requiresSelection: true,
        button: {
            name: i18n.t('common:button:delete'),
            toolbar: true,
            icon: ChonkyIconName.trash
        }
    });

    return {CreateFolderAction, DeleteFileAction};
};

export default useFileAction;
