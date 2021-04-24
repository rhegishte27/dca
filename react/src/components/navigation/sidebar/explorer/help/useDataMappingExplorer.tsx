import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import { MenuItem } from '../../../MenuItem';

const useDataMappingExplorer = () => {

    const {i18n} = useTranslation('navigation');

    const getNodeDataMapping = (parentId: string): ExplorerNode => {
        const id: string = parentId + 'dataMapping';
        return {
            id: id,
            title: i18n.t('navigation:help:dataMapping'),
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
        getNodeDataMapping
    };
};

export default useDataMappingExplorer;