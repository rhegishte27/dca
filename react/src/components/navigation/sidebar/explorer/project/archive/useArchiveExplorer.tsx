import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../../lib/domain/entities/Feature';
import { MenuItem } from '../../../../MenuItem';
import useListArchiveExplorer from './useListArchiveExplorer';

const useArchiveExplorer = () => {
    const {i18n} = useTranslation('navigation');
    const {getListArchiveNodes} = useListArchiveExplorer();

    const getNodeArchive = (parentId: string): ExplorerNode => {
        const id = parentId + 'archives';
        return {
            id: id,
            title: i18n.t('navigation:project:archives'),
            contextMenu: getRootMenuContext(),
            isDirectory: true,
            children: getListArchiveNodes(id)
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:create'),
                    features: [Feature.PROJECT_MANAGEMENT]
                }
            ],

            [
                {
                    title: i18n.t('navigation:settings'),
                    features: [Feature.PROJECT_MANAGEMENT]
                }
            ],

            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.PROJECT_MANAGEMENT]
                }
            ]
        ];
    };

    return {
        getNodeArchive
    };
};

export default useArchiveExplorer;