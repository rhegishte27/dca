import { useTranslation } from 'react-i18next';
import Feature from '../../../lib/domain/entities/Feature';
import { MenuItem } from '../MenuItem';
import useMenuItemActions from '../useMenuItemActions';

export const useFileMenu = () => {
    const { i18n } = useTranslation(['navigation']);
    const { openForm, logout } = useMenuItemActions();

    const getFileMenuTitle = ():string => {
        return i18n.t('navigation:dca:file');
    };

    const getFileMenuItems = (): MenuItem[] => {
        return [
            {
                title: i18n.t('navigation:settings'),
                features: [Feature.SYSTEM_SETTINGS],
                onClick: () => openForm('Setting')
            },
            {
                title: i18n.t('navigation:tools'),
                items: [
                    {
                        title: i18n.t('navigation:dca:generateGUIDs')
                    },
                    {
                        title: i18n.t('navigation:dca:viewFileContents')
                    }
                ]
            },
            {
                title: i18n.t('navigation:dca:updates'),
                items: [
                    {
                        title: i18n.t('navigation:dca:upgrade')
                    },
                    {
                        title: i18n.t('navigation:dca:uploadCodeGenerator')
                    },
                    {
                        title: i18n.t('navigation:dca:undoLastUpload')
                    },
                ]
            },
            {
                title: i18n.t('navigation:dca:exit'),
                onClick: logout
            }
        ];
    };

    return { getFileMenuTitle, getFileMenuItems };
};
