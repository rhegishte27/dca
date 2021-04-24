import React, { ChangeEvent, useState } from 'react';
import { EntityType } from '../../features/common/EntityType';
import { sortList } from '../../features/common/listUtil';
import { useMainAreaContext } from '../editor/MainAreaProvider';
import useMenuItemActions from '../navigation/useMenuItemActions';

const useCustomList = <T>(getItemId: (o: T) => string,
                          getItemIdentifier: (o: T) => any,
                          findAll: () => Promise<T[]>,
                          entityType: EntityType) => {

    const [lstItem, setLstItem] = useState<T[]>([]);
    const [lstDelete, setLstDelete] = useState<string[]>([]);
    const { openForm, deleteItem, deleteItems } = useMenuItemActions();
    const { refreshMainAreaListPage, updateMainAreaListPage } = useMainAreaContext();

    React.useEffect(() => {
        loadItems();
    }, []);

    React.useEffect(() => {
        updateMainAreaListPage(loadItems);
    }, [refreshMainAreaListPage]);

    const loadItems = async () => {
        await findAll().then((lst) => {
            setLstItem(sortList(lst, getItemIdentifier));

            const listId: string[] = lst.map(i => getItemId(i).toString());
            const newListDelete: string[] = lstDelete.filter(i => listId.includes(i.toString()));
            setLstDelete(newListDelete);
        });
    };

    const add = async (e: React.MouseEvent<HTMLElement>) => {
        e.preventDefault();
        openForm(entityType);
    };

    const edit = async (id: string, e: React.MouseEvent<HTMLElement>) => {
        e.preventDefault();
        openForm(entityType, id);
    };

    const remove = async (item: T) => {
        deleteItem(entityType, item);
    };

    const removeItems = () => {
        const lstItemDelete = lstDelete.map(getItemFromLstItem);
        deleteItems(entityType, lstItemDelete);
    };

    const getItemFromLstItem = (id: string): T | undefined => {
        return lstItem.find(i => getItemId(i).toString() === id.toString());
    };

    const checkBoxChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        const input = e.target.value;

        let lstClone: string[] = [...lstDelete];

        if (isChecked(input)) {
            lstClone = lstDelete.filter(id => id.toString() !== input.toString());
        } else {
            lstClone.push(input);
        }
        setLstDelete(lstClone);
    };

    const selectAllHandler = () => {
        const lst: string[] = [];

        if (!isCheckedAll()) {
            lstItem.forEach(o => {
                lst.push(getItemId(o));
            });
        }

        setLstDelete(lst);
    };

    const isChecked = (id: string): boolean => {
        return !!lstDelete.find(o => o.toString() === id.toString());
    };

    const isCheckedAll = (): boolean => {
        return lstDelete.length === lstItem.length;
    };

    const isLstDeleteEmpty = (): boolean => {
        return lstDelete.length === 0;
    };

    return {
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
    };
};

export default useCustomList;