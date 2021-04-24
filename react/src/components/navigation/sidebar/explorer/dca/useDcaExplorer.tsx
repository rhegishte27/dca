import { useTranslation } from 'react-i18next';
import { ExplorerNode, TreeExplorerData } from '../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../lib/domain/entities/Feature';
import { MenuItem } from '../../../MenuItem';
import useMenuItemActions from '../../../useMenuItemActions';
import useHelpExplorer from '../help/useHelpExplorer';
import useLocationExplorer from '../location/useLocationExplorer';
import useOrganizationExplorer from '../organization/useOrganizationExplorer';
import useProjectExplorer from '../project/useProjectExplorer';
import useSystemExplorer from '../system/useSystemExplorer';
import useUserExplorer from '../user/useUserExplorer';

const useDcaExplorer = () => {
    const {i18n} = useTranslation(['common', 'navigation']);
    const { openForm } = useMenuItemActions();

    const { getNodeOrganization } = useOrganizationExplorer();
    const { getNodeProject } = useProjectExplorer();
    const { getNodeSystem } = useSystemExplorer();
    const { getNodeUser } = useUserExplorer();
    const { getNodeLocation } = useLocationExplorer();
    const { getNodeHelp } = useHelpExplorer();

    const getNodeDca = (treeExplorerData: TreeExplorerData, parentId?: string): ExplorerNode => {
        const id = parentId ? parentId : '' + 'dca';
        return {
            id: id,
            title: i18n.t('common:dca'),
            isDirectory: true,
            contextMenu: getRootMenuContext(),
            children: [
                getNodeLocation(treeExplorerData.locations, id),
                getNodeProject(treeExplorerData.projects, id),
                getNodeSystem(treeExplorerData.systems, id),
                getNodeOrganization(treeExplorerData.organizations, id),
                getNodeUser(treeExplorerData.users, id),
                getNodeHelp(id)
            ]
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:dca:refresh')
                },
                {
                    title: i18n.t('navigation:settings'),
                    features: [Feature.SYSTEM_SETTINGS],
                    onClick: () => openForm('Setting')
                }
            ],

            [
                {
                    title: i18n.t('navigation:search')
                }
            ],

            [
                {
                    title: i18n.t('navigation:dca:sampleFiles')
                },
                {
                    title: i18n.t('navigation:dca:utilityFiles')
                },
                {
                    title: i18n.t('navigation:dca:generateGUIDs')
                },
                {
                    title: i18n.t('navigation:dca:viewFileContents')
                }
            ],

            [
                {
                    title: i18n.t('navigation:dca:updates'),
                    features: [Feature.SYSTEM_SETTINGS]
                },
                {
                    title: i18n.t('navigation:dca:uploadCodeGenerator'),
                    features: [Feature.SYSTEM_SETTINGS]
                },
                {
                    title: i18n.t('navigation:dca:undoLastUpload'),
                    features: [Feature.SYSTEM_SETTINGS]
                }
            ],

            [
                {
                    title: i18n.t('navigation:exit')
                }
            ]
        ];
    };

    return {
        getNodeDca
    };
};

export default useDcaExplorer;