import { Button, Checkbox } from 'equisoft-design-ui-elements';
import React from 'react';
import { useTranslation } from 'react-i18next';
import ReactTooltip from 'react-tooltip';
import useCustomList from '../../components/list/List';
import { defaultLocationService } from '../../lib/context';
import BaseEnum from '../../lib/domain/entities/BaseEnum';
import Location from '../../lib/domain/entities/Location';
import LocationType from '../../lib/domain/entities/LocationType';
import LocationService from '../../lib/services/LocationService';
import { Actions, FormTitle, MediumForm, PanelBreak, TableAndActions } from '../common/style';

interface Props {
    locationService?: LocationService;

    openForm(data?: Location): void;
}

const LocationList: React.FC<Props> = ({ locationService = defaultLocationService }) => {
    const { i18n } = useTranslation(['common', 'message', 'location']);

    const getItemId = (o: Location): string => {
        return o.id;
    };

    const getItemIdentifier = (o: Location): string => {
        return o.identifier;
    };

    const findAll = async (): Promise<Location[]> => {
        return locationService.findAll();
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
        'Location'
    );

    return (
        <>
            <MediumForm>
                <FormTitle>{i18n.t('location:titlePlural')}</FormTitle>
                <>
                    <PanelBreak/>
                    <Actions>
                        <Button buttonType="secondary" onClick={add}>
                            {i18n.t('common:button:add', {what: i18n.t('location:title')})}
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
                                    <th>{i18n.t('location:identifier')}</th>
                                    <th>{i18n.t('location:locationType')}</th>
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
                                        <td>{i18n.t(BaseEnum.getValue(LocationType, item.locationType.id).name)}</td>
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

export default LocationList;
