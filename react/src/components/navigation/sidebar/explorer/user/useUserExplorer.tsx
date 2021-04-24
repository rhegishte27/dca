import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../lib/domain/entities/Feature';
import User from '../../../../../lib/domain/entities/User';
import { MenuItem } from '../../../MenuItem';
import useMenuItemActions from '../../../useMenuItemActions';
import useListUserExplorer from './useListUserExplorer';

const useUserExplorer = () => {
    const { getListUserNodes } = useListUserExplorer();
    const {i18n} = useTranslation(['navigation']);
    const { openForm } = useMenuItemActions();

    const getNodeUser = (users: User[], parentId: string): ExplorerNode => {
        const id: string = parentId + 'user';
        return {
            id: id,
            title: i18n.t('navigation:user:users'),
            contextMenu: getRootMenuContext(),
            isDirectory: true,
            children: getListUserNodes(users, id)
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:create'),
                    features: [Feature.ORGANIZATIONS_AND_USERS],
                    onClick: () => openForm('User')
                },
                {
                    title: i18n.t('navigation:view'),
                    features: [Feature.ORGANIZATIONS_AND_USERS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:user:viewLogonFailures'),
                    features: [Feature.ORGANIZATIONS_AND_USERS]
                },
                {
                    title: i18n.t('navigation:user:clearLogonFailures'),
                    features: [Feature.ORGANIZATIONS_AND_USERS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:user:viewActivityLog'),
                    features: [Feature.ORGANIZATIONS_AND_USERS]
                },
                {
                    title: i18n.t('navigation:user:clearActivityLog'),
                    features: [Feature.ORGANIZATIONS_AND_USERS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:user:viewReservations'),
                    features: [Feature.ORGANIZATIONS_AND_USERS]
                },
                {
                    title: i18n.t('navigation:user:releaseReservations'),
                    features: [Feature.ORGANIZATIONS_AND_USERS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.ORGANIZATIONS_AND_USERS]
                }
            ]
        ];
    };

    return {
        getNodeUser
    };
};

export default useUserExplorer;