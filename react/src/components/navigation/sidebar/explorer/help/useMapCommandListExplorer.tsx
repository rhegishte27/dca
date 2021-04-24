import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import { MenuItem } from '../../../MenuItem';

const useMapCommandListExplorer = () => {
    const {i18n} = useTranslation('navigation');

    const getNodeMapCommandList = (parentId: string): ExplorerNode => {
        const id: string = parentId + 'mapCommandList';
        return {
            id: id,
            title: i18n.t('navigation:help:mapCommandList'),
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
        getNodeMapCommandList
    };
};

export default useMapCommandListExplorer;