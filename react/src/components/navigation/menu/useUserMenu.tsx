import { useTranslation } from 'react-i18next';
import { EntityType } from '../../../features/common/EntityType';
import Feature from '../../../lib/domain/entities/Feature';
import User from '../../../lib/domain/entities/User';
import { useAuthState } from '../../../page/authContext';
import { MenuItem } from '../MenuItem';
import useMenuItemActions from '../useMenuItemActions';

export const useUserMenu = (toggleDialog: (modalType: EntityType) => void) => {

    const {i18n} = useTranslation(['navigation']);
    const { openForm, deleteItem } = useMenuItemActions();
    const { isSameUserAsLoggedInUser, isLoggedInUserHasHigherOrEqualsRole } = useAuthState();

    const getUserMenuTitle = (): string => {
        return i18n.t('navigation:user:users');
    };

    const getUserMenuItems = (defaultUser: User | undefined): MenuItem[] => {
        const userIdentifier = defaultUser ? ' - ' + defaultUser.identifier : '';
        const idUser = defaultUser ? defaultUser.id : undefined;
        const featuresIfEditHisOwnUser = isSameUserAsLoggedInUser(defaultUser) ? undefined : [Feature.ORGANIZATIONS_AND_USERS];
        const editable = isSameUserAsLoggedInUser(defaultUser) || isLoggedInUserHasHigherOrEqualsRole(defaultUser);
        const deletable = !isSameUserAsLoggedInUser(defaultUser) && isLoggedInUserHasHigherOrEqualsRole(defaultUser);
        return [
            {
                title: i18n.t('navigation:set') + userIdentifier,
                features: [Feature.ORGANIZATIONS_AND_USERS],
                onClick: () => toggleDialog('User')
            },
            {
                title: i18n.t('navigation:manage'),
                items: [
                    {
                        title: i18n.t('navigation:create'),
                        features: [Feature.ORGANIZATIONS_AND_USERS],
                        onClick: () => openForm('User')
                    },
                    {
                        title: i18n.t('navigation:edit'),
                        features: featuresIfEditHisOwnUser,
                        disable: !defaultUser || !editable,
                        onClick: () => openForm('User', idUser)
                    },
                    {
                        title: i18n.t('navigation:view'),
                        features: featuresIfEditHisOwnUser
                    },
                    {
                        title: i18n.t('navigation:delete'),
                        features: featuresIfEditHisOwnUser,
                        disable: !defaultUser || !deletable,
                        onClick: () => deleteItem('User', defaultUser)
                    }
                ]
            },
            {
                title: i18n.t('navigation:tools'),
                items: [
                    {
                        title: i18n.t('navigation:user:logonFailures'),
                        items: [
                            {
                                title: i18n.t('navigation:view'),
                                features: featuresIfEditHisOwnUser
                            },
                            {
                                title: i18n.t('navigation:user:clear'),
                                features: featuresIfEditHisOwnUser
                            }
                        ]
                    },
                    {
                        title: i18n.t('navigation:user:activityLog'),
                        items: [
                            {
                                title: i18n.t('navigation:view'),
                                features: featuresIfEditHisOwnUser
                            },
                            {
                                title: i18n.t('navigation:user:clear'),
                                features: featuresIfEditHisOwnUser
                            }
                        ]
                    },
                    {
                        title: i18n.t('navigation:user:reservations'),
                        items: [
                            {
                                title: i18n.t('navigation:view'),
                                features: featuresIfEditHisOwnUser
                            },
                            {
                                title: i18n.t('navigation:user:release'),
                                features: featuresIfEditHisOwnUser
                            }
                        ]
                    },
                ]
            }
        ];
    };

    return { getUserMenuTitle, getUserMenuItems };
};
