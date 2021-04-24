import { Button, Checkbox } from 'equisoft-design-ui-elements';
import React from 'react';
import { useTranslation } from 'react-i18next';
import ReactTooltip from 'react-tooltip';
import useCustomList from '../../components/list/List';
import { defaultSystemService } from '../../lib/context';
import System from '../../lib/domain/entities/System';
import SystemService from '../../lib/services/SystemService';
import { Actions, FormTitle, MediumForm, PanelBreak, TableAndActions } from '../common/style';

interface Props {
    systemService?: SystemService;

    openForm(data?: System): void;
}

const SystemList: React.FC<Props> = ({ systemService = defaultSystemService }) => {
    const { i18n } = useTranslation(['common', 'message', 'system']);

    const getItemId = (o: System): string => {
        return o.id;
    };

    const getItemIdentifier = (o: System): string => {
        return o.identifier;
    };

    const findAll = async (): Promise<System[]> => {
        return systemService.findAll();
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
        'System'
    );

    return (
        <>
            <MediumForm>
                <FormTitle>{i18n.t('system:titlePlural')}</FormTitle>
                <>
                    <PanelBreak/>
                    <Actions>
                        <Button buttonType="secondary" onClick={add}>
                            {i18n.t('common:button:add', {what: i18n.t('system:title')})}
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
                                    <th>{i18n.t('system:identifier')}</th>
                                    <th>{i18n.t('system:description')}</th>
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

                                        <td data-tip={item.description} data-for="descriptionTip">
                                            {item.description.substring(0, 30)}
                                        </td>
                                        <td>
                                            <Actions>
                                                <Button type={'button'} buttonType={'tertiary'} onClick={e => edit(item.id, e)}>
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

export default SystemList;
