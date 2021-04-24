import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import { MenuItem } from '../../../MenuItem';
import useMenuItemActions from '../../../useMenuItemActions';
import useDataMappingExplorer from './useDataMappingExplorer';
import useGettingStartedExplorer from './useGettingStartedExplorer';
import useMapCommandListExplorer from './useMapCommandListExplorer';
import useNavigationExplorer from './useNavigationExplorer';

const useHelpExplorer = () => {
    const {getNodeGettingStarted} = useGettingStartedExplorer();
    const {getNodeNavigation} = useNavigationExplorer();
    const {getNodeDataMapping} = useDataMappingExplorer();
    const {getNodeMapCommandList} = useMapCommandListExplorer();

    const {i18n} = useTranslation('navigation');
    const { openLink } = useMenuItemActions();

    const getNodeHelp = (parentId: string): ExplorerNode => {
        const id: string = parentId + 'help';
        return {
            id: id,
            title: i18n.t('navigation:help:help'),
            contextMenu: getRootMenuContext(),
            isDirectory: true,
            children: [
                getNodeGettingStarted(id),
                getNodeNavigation(id),
                getNodeDataMapping(id),
                getNodeMapCommandList(id)
            ]
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:help:uctWebsite'),
                    onClick: () => openLink('http://' + i18n.t('navigation:help:uctWebsite'))
                },
                {
                    title: i18n.t('navigation:help:about')
                }
            ]
        ];
    };

    return {
        getNodeHelp
    };
};

export default useHelpExplorer;