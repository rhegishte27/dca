import { useTranslation } from 'react-i18next';
import { sortList } from '../../../../../features/common/listUtil';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../lib/domain/entities/Feature';
import User from '../../../../../lib/domain/entities/User';
import { useAuthState } from '../../../../../page/authContext';
import { MenuItem } from '../../../MenuItem';
import useMenuItemActions from '../../../useMenuItemActions';

const useListUserExplorer = () => {
    const {i18n} = useTranslation(['user', 'navigation']);
    const { openForm, deleteItem } = useMenuItemActions();
    const { isSameUserAsLoggedInUser, isLoggedInUserHasHigherOrEqualsRole } = useAuthState();

    const getListUserNodes = (users: User[], parentId: string): ExplorerNode[] => {
        const nodes: ExplorerNode[] = [];
        sortList(users, (o => o.identifier))
            .map(u => {
                const id = parentId + u.id;
                nodes.push({
                    id: id,
                    title: u.identifier,
                    contextMenu: getChildMenuContext(u),
                    isDirectory: false
                });
            });

        return nodes;
    };

    const getChildMenuContext = (user: User): MenuItem[][] => {
        const featuresIfEditHisOwnUser: Feature[] | undefined = isSameUserAsLoggedInUser(user) ? undefined : [Feature.ORGANIZATIONS_AND_USERS];
        const editable = isSameUserAsLoggedInUser(user) || isLoggedInUserHasHigherOrEqualsRole(user);
        const deletable = !isSameUserAsLoggedInUser(user) && isLoggedInUserHasHigherOrEqualsRole(user);
        return [
            [
                {
                    title: i18n.t('navigation:edit'),
                    features: featuresIfEditHisOwnUser,
                    disable: !editable,
                    onClick: () => openForm('User', user.id)
                }
            ],
            [
                {
                    title: i18n.t('navigation:user:viewActivityLog'),
                    disable: !editable,
                    features: featuresIfEditHisOwnUser
                }
            ],
            [
                {
                    title: i18n.t('navigation:delete'),
                    features: featuresIfEditHisOwnUser,
                    disable: !deletable,
                    onClick: () => deleteItem('User', user)
                }
            ]
        ];
    };

    return {
        getListUserNodes
    };
};

export default useListUserExplorer;