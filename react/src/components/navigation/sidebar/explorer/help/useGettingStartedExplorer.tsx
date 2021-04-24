import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import { MenuItem } from '../../../MenuItem';

const useGettingStartedExplorer = () => {
    const {i18n} = useTranslation('navigation');

    const getNodeGettingStarted = (parentId: string): ExplorerNode => {
        const id: string = parentId + 'gettingStarted';
        return {
            id: id,
            title: i18n.t('navigation:help:gettingStarted'),
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
        getNodeGettingStarted
    };
};

export default useGettingStartedExplorer;