import { Button, Checkbox } from 'equisoft-design-ui-elements';
import React from 'react';
import { useTranslation } from 'react-i18next';
import ReactTooltip from 'react-tooltip';
import useCustomList from '../../components/list/List';
import { defaultOrganizationService } from '../../lib/context';
import Organization from '../../lib/domain/entities/Organization';
import OrganizationService from '../../lib/services/OrganizationService';
import { Actions, FormTitle, MediumForm, PanelBreak, TableAndActions } from '../common/style';

interface Props {
    organizationService?: OrganizationService;

    openForm(data?: Organization): void;
}

const OrganizationList: React.FC<Props> = ({ organizationService = defaultOrganizationService }) => {
    const { i18n } = useTranslation(['common', 'message', 'organization']);

    const getItemId = (o: Organization): string => {
        return o.id;
    };

    const getItemIdentifier = (o: Organization): string => {
        return o.name;
    };

    const findAll = async (): Promise<Organization[]> => {
        return organizationService.findAll();
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
        'Organization'
    );

    return (
        <>
            <MediumForm>
                <FormTitle>{i18n.t('organization:titlePlural')}</FormTitle>
                <>
                    <PanelBreak/>
                    <Actions>
                        <Button buttonType="secondary" onClick={add}>
                            {i18n.t('common:button:add', {what: i18n.t('organization:title')})}
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
                                    <th>{i18n.t('organization:name')}</th>
                                    <th>{i18n.t('organization:description')}</th>
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

                                        <td>{item.name}</td>

                                        <td data-tip={item.description} data-for="descriptionTip">
                                            {item.description.substring(0, 30)}
                                        </td>
                                        <td>
                                            <Actions>
                                                <Button type="button" buttonType={'tertiary'} onClick={e => edit(item.id, e)}>
                                                    {i18n.t('common:button:edit')}
                                                </Button>
                                                <Button type={'button'} buttonType={'tertiary'} onClick={() => remove(item)}>
                                                    {i18n.t('common:button:delete')}
                                                </Button>
                                            </Actions>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </TableAndActions>
                            <ReactTooltip id="descriptionTip" place="right" multiline />
                        </>
                    )}
                </>
            </MediumForm>
        </>
    );
};

export default OrganizationList;
