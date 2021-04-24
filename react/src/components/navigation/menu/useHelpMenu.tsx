import { useTranslation } from 'react-i18next';
import { MenuItem } from '../MenuItem';
import useMenuItemActions from '../useMenuItemActions';

export const useHelpMenu = () => {
    const {i18n} = useTranslation(['navigation']);
    const { openLink } = useMenuItemActions();

    const getHelpMenuTitle = ():string => {
        return i18n.t('navigation:help:help');
    };

    const getHelpMenuItems = (): MenuItem[] => {
        return [
            {
                title: i18n.t('navigation:help:uctWebsite'),
                onClick: () => openLink('http://' + i18n.t('navigation:help:uctWebsite'))
            },
            {
                title: i18n.t('navigation:help:gettingStarted')
            },
            {
                title: i18n.t('navigation:help:navigation')
            },
            {
                title: i18n.t('navigation:help:dataMapping')
            },
            {
                title: i18n.t('navigation:help:mapCommandList')
            },
            {
                title: i18n.t('navigation:help:about')
            }
        ];
    };

    return { getHelpMenuTitle, getHelpMenuItems };
};
