import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../../lib/domain/entities/Feature';
import { MenuItem } from '../../../../MenuItem';
import useListConfigurationExplorer from './useListConfigurationExplorer';

const useConfigurationExplorer = () => {
    const {getListConfigurationNodes} = useListConfigurationExplorer();
    const {i18n} = useTranslation('navigation');

    const getNodeConfiguration = (parentId: string): ExplorerNode => {
        const id: string = parentId + 'configuration';
        return {
            id: id,
            title: i18n.t('navigation:system:configuration'),
            contextMenu: getRootMenuContext(),
            isDirectory: true,
            children: getListConfigurationNodes(id)
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:import'),
                    features: [Feature.SYSTEM_MANAGEMENT]
                }
            ],
            [
                {
                    title: i18n.t('navigation:view'),
                    features: [Feature.SYSTEM_MANAGEMENT]
                }
            ],
            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.SYSTEM_MANAGEMENT]
                }
            ]
        ];
    };

    return {
        getNodeConfiguration
    };
};

export default useConfigurationExplorer;