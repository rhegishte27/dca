import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../../lib/domain/entities/Feature';
import { MenuItem } from '../../../../MenuItem';
import useListMapExplorer from './useListMapExplorer';

const useMapExplorer = () => {
    const {i18n} = useTranslation('navigation');
    const {getListMapNodes} = useListMapExplorer();

    const getNodeMap = (parentId: string): ExplorerNode => {
        const id = parentId + 'maps';
        return {
            id: id,
            title: i18n.t('navigation:project:maps'),
            contextMenu: getRootMenuContext(),
            isDirectory: true,
            children: getListMapNodes(id)
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:create'),
                    features: [Feature.PROJECT_DATA_MAPS]
                },
                {
                    title: i18n.t('navigation:project:createTemplate'),
                    features: [Feature.PROJECT_DATA_MAPS]
                },
                {
                    title: i18n.t('navigation:import'),
                    features: [Feature.PROJECT_DATA_MAPS]
                },
                {
                    title: i18n.t('navigation:copy'),
                    features: [Feature.PROJECT_DATA_MAPS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:search'),
                    features: [Feature.PROJECT_DATA_MAPS]
                },
                {
                    title: i18n.t('navigation:viewMessages'),
                    features: [Feature.PROJECT_DATA_MAPS]
                },
                {
                    title: i18n.t('navigation:validate'),
                    features: [Feature.PROJECT_DATA_MAPS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.PROJECT_DATA_MAPS]
                }
            ]
        ];
    };

    return {
        getNodeMap
    };
};

export default useMapExplorer;