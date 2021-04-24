import { Button, Checkbox } from 'equisoft-design-ui-elements';
import React from 'react';
import { useTranslation } from 'react-i18next';
import useCustomList from '../../components/list/List';
import { defaultUserService } from '../../lib/context';
import User from '../../lib/domain/entities/User';
import UserService from '../../lib/services/UserService';
import { useAuthState } from '../../page/authContext';
import { Actions, FormTitle, MediumForm, PanelBreak, TableAndActions } from '../common/style';

interface Props {
    userService?: UserService;
    openForm(data?: User): void;
}

const UserList: React.FC<Props> = ({ userService = defaultUserService }) => {
    const { i18n } = useTranslation(['common', 'message', 'user']);
    const { isSameUserAsLoggedInUser, isLoggedInUserHasHigherOrEqualsRole } = useAuthState();

    const getItemId = (o: User): string => {
        return o.id;
    };

    const getItemIdentifier = (o: User): string => {
        return o.identifier;
    };

    const findAll = async (): Promise<User[]> => {
        return userService.findAll();
    };

    const {
        lstItem,
        add,
        edit,
        remove,
        removeItems,
        checkBoxChangeHandler,
        selectAllHandler,
        isChecked,
        isCheckedAll,
        isLstDeleteEmpty
    } = useCustomList(
        getItemId,
        getItemIdentifier,
        findAll,
        'User'
    );

    const canModify = (user: User): boolean => {
        return isSameUserAsLoggedInUser(user) || isLoggedInUserHasHigherOrEqualsRole(user);
    };

    return (
        <>
            <MediumForm>
                <FormTitle>{i18n.t('user:titlePlural')}</FormTitle>
                <>
                    <PanelBreak/>
                    <Actions>
                        <Button buttonType={'secondary'} onClick={add}>
                            {i18n.t('common:button:add', {what: i18n.t('user:title')})}
                        </Button>
                        <Button type={'button'} onClick={removeItems} buttonType={'secondary'} disabled={isLstDeleteEmpty()}>
                            {i18n.t('common:button:delete')}
                        </Button>
                    </Actions>
                </>
                <>
                    <PanelBreak/>
                    {lstItem.length > 0 && (
                        <>
                            <TableAndActions>
                                <thead>
                                <tr>
                                    <th>
                                        <Checkbox
                                            label={''}
                                            name={''}
                                            value={''}
                                            onChange={selectAllHandler}
                                            checked={isCheckedAll()}
                                        />
                                    </th>
                                    <th>{i18n.t('user:identifier')}</th>
                                    <th>{i18n.t('user:userName')}</th>
                                    <th>{i18n.t('user:organization')}</th>
                                    <th>{i18n.t('common:actions')}</th>
                                </tr>
                                </thead>
                                <tbody>
                                {lstItem.map((item) => (
                                    <tr key={item.id}>
                                        <td>
                                            <Checkbox
                                                label={''}
                                                name={''}
                                                value={item.id}
                                                onChange={checkBoxChangeHandler}
                                                checked={isChecked(item.id)}
                                            />
                                        </td>

                                        <td>{item.identifier}</td>

                                        <td>{item.name}</td>

                                        <td>{item.organization.name}</td>
                                        <td>
                                            <Actions>
                                                <Button disabled={!canModify(item)} type={'button'} buttonType={'tertiary'} onClick={e => edit(item.id, e)}>
                                                    {i18n.t('common:button:edit')}
                                                </Button>
                                                <Button disabled={!canModify(item)} type={'button'} buttonType={'tertiary'} onClick={() => remove(item)}>
                                                    {i18n.t('common:button:delete')}
                                                </Button>
                                            </Actions>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </TableAndActions>
                        </>
                    )}
                </>
            </MediumForm>
        </>
    );
};

export default UserList;
