import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import { MenuItem } from '../../../MenuItem';

const useNavigationExplorer = () => {

    const {i18n} = useTranslation('navigation');

    const getNodeNavigation = (parentId: string): ExplorerNode => {
        const id: string = parentId + 'navigation';
        return {
            id: id,
            title: i18n.t('navigation:help:navigation'),
            contextMenu: getRootMenuContext(),
            isDirectory: false
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:open')
                }
            ]
        ];
    };

    return {
        getNodeNavigation
    };
};

export default useNavigationExplorer;