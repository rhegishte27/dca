import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../lib/domain/entities/Feature';
import Project from '../../../../../lib/domain/entities/Project';
import { MenuItem } from '../../../MenuItem';
import useMenuItemActions from '../../../useMenuItemActions';
import useListProjectExplorer from './useListProjectExplorer';

const useProjectExplorer = () => {
    const { getListProjectNodes } = useListProjectExplorer();
    const {i18n} = useTranslation(['navigation']);
    const { openForm } = useMenuItemActions();

    const getNodeProject = (projects: Project[], parentId: string): ExplorerNode => {
        const id = parentId + 'project';
        return {
            id: id,
            title: i18n.t('navigation:project:projects'),
            contextMenu: getRootMenuContext(),
            isDirectory: true,
            children: getListProjectNodes(projects, id)
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {

        return [
            [
                {
                    title: i18n.t('navigation:create'),
                    features: [Feature.PROJECT_MANAGEMENT],
                    onClick: () => openForm('Project')
                },
                {
                    title: i18n.t('navigation:import'),
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
                    title: i18n.t('navigation:delete'),
                    features: [Feature.PROJECT_MANAGEMENT]
                }
            ]
        ];
    };

    return {
        getNodeProject
    };
};

export default useProjectExplorer;