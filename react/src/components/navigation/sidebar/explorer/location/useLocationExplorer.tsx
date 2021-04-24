import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../lib/domain/entities/Feature';
import Location from '../../../../../lib/domain/entities/Location';
import { MenuItem } from '../../../MenuItem';
import useMenuItemActions from '../../../useMenuItemActions';
import useListLocationExplorer from './useListLocationExplorer';

const useLocationExplorer = () => {
    const { getListLocationNodes } = useListLocationExplorer();
    const { openForm } = useMenuItemActions();
    const { i18n } = useTranslation(['navigation']);

    const getNodeLocation = (locations: Location[], parentId: string): ExplorerNode => {
        const id: string = parentId + 'location';
        return {
            id: id,
            title: i18n.t('navigation:location:locations'),
            contextMenu: getRootMenuContext(),
            isDirectory: true,
            children: getListLocationNodes(locations, id)
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:create'),
                    features: [Feature.SYSTEM_SETTINGS],
                    onClick: () => openForm('Location')
                }
            ],

            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.SYSTEM_SETTINGS]
                }
            ]
        ];
    };

    return {
        getNodeLocation
    };
};

export default useLocationExplorer;