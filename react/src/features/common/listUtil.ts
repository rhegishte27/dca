export const sortList = <T>(list: T[], getFieldToCompare: (o: T) => any): T[] => {
    const newList = [...list];

    return newList.sort((a, b) => {
        const field1 = getFieldToCompare(a);
        const field2 = getFieldToCompare(b);

        if (!field1 && !field2) {
            return 0;
        }

        if (!field1) {
            return -1;
        }

        if (!field2) {
            return 1;
        }

        return getFieldToCompare(a).toString().localeCompare(getFieldToCompare(b).toString());
    });
};