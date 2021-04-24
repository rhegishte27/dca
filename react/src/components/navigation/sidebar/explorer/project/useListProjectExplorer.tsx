import { useTranslation } from 'react-i18next';
import { sortList } from '../../../../../features/common/listUtil';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../lib/domain/entities/Feature';
import Project from '../../../../../lib/domain/entities/Project';
import { MenuItem } from '../../../MenuItem';
import useMenuItemActions from '../../../useMenuItemActions';
import useArchiveExplorer from './archive/useArchiveExplorer';
import useMapExplorer from './map/useMapExplorer';
import useTableExplorer from './table/useTableExplorer';

const useListProjectExplorer = () => {
    const {getNodeMap} = useMapExplorer();
    const {getNodeTable} = useTableExplorer();
    const {getNodeArchive} = useArchiveExplorer();

    const {i18n} = useTranslation('navigation');
    const { openForm, deleteItem } = useMenuItemActions();

    const getListProjectNodes = (projects: Project[], parentId: string): ExplorerNode[] => {
        const nodes: ExplorerNode[] = [];

        sortList(projects, (o => o.identifier))
            .map(p => {
                const id = parentId + p.id;
                nodes.push({
                    id: id,
                    title: p.identifier,
                    contextMenu: getChildMenuContext(p),
                    isDirectory: true,
                    children: [
                        getNodeMap(id),
                        getNodeTable(id),
                        getNodeArchive(id)
                    ]
                });
            });

        return nodes;
    };

    const getChildMenuContext = (project: Project): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:edit'),
                    features: [Feature.PROJECT_MANAGEMENT],
                    onClick: () => openForm('Project', project.id)
                },
                {
                    title: i18n.t('navigation:details'),
                    features: [Feature.PROJECT_MANAGEMENT]
                },
                {
                    title: i18n.t('navigation:export'),
                    features: [Feature.PROJECT_MANAGEMENT]
                },
                {
                    title: i18n.t('navigation:download'),
                    features: [Feature.PROJECT_MANAGEMENT]
                },
                {
                    title: i18n.t('navigation:copy'),
                    features: [Feature.PROJECT_MANAGEMENT]
                }
            ],
            [
                {
                    title: i18n.t('navigation:search'),
                    features: [Feature.PROJECT_DATA_MAPS, Feature.PROJECT_TABLES]
                }
            ],
            [
                {
                    title: i18n.t('navigation:project:buildCsvInputProgram'),
                    features: [Feature.PROJECT_DATA_MAPS]
                },
                {
                    title: i18n.t('navigation:project:buildCsvOutputProgram'),
                    features: [Feature.PROJECT_DATA_MAPS]
                },
                {
                    title: i18n.t('navigation:project:buildJsonOutputProgram'),
                    features: [Feature.PROJECT_DATA_MAPS]
                },
                {
                    title: i18n.t('navigation:project:buildOIPAOutputProgram'),
                    features: [Feature.PROJECT_DATA_MAPS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:project:transferAll'),
                    features: [Feature.PROJECT_MANAGEMENT]
                },
                {
                    title: i18n.t('navigation:project:viewTransferError'),
                    features: [Feature.PROJECT_MANAGEMENT]
                }
            ],
            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.PROJECT_MANAGEMENT],
                    onClick: () => deleteItem('Project', project)
                }
            ]
        ];
    };

    return {
        getListProjectNodes
    };
};

export default useListProjectExplorer;