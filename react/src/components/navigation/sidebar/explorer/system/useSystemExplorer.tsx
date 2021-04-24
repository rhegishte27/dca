import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../lib/domain/entities/Feature';
import System from '../../../../../lib/domain/entities/System';
import { MenuItem } from '../../../MenuItem';
import useMenuItemActions from '../../../useMenuItemActions';
import useListSystemExplorer from './useListSystemExplorer';

const useSystemExplorer = () => {
    const { getListSystemNodes } = useListSystemExplorer();

    const {i18n} = useTranslation(['navigation']);
    const { openForm } = useMenuItemActions();

    const getNodeSystem = (systems: System[], parentId: string): ExplorerNode => {
        const id: string = parentId + 'system';
        return {
            id: id,
            title: i18n.t('navigation:system:systems'),
            contextMenu: getRootMenuContext(),
            isDirectory: true,
            children: getListSystemNodes(systems, id)
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:create'),
                    features: [Feature.SYSTEM_MANAGEMENT],
                    onClick: () => openForm('System')
                }
            ],
            [
                {
                    title: i18n.t('navigation:compare'),
                    features: [Feature.SYSTEM_MANAGEMENT]
                },
                {
                    title: i18n.t('navigation:adjustNames'),
                    features: [Feature.SYSTEM_MANAGEMENT]
                },
                {
                    title: i18n.t('navigation:search'),
                    features: [Feature.SYSTEM_DATA_OBJECTS]
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
        getNodeSystem
    };
};

export default useSystemExplorer;