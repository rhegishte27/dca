import BaseEntity from './BaseEntity';

export default abstract class BaseEnum extends BaseEntity {
    public id: string;
    public name: string;

    protected constructor(id?: string, name?: string) {
        super();
        this.id = id ? id : '';
        this.name = name ? name : '';
    }

    public static findValue<T extends BaseEnum>(type: new() => T, id: string): T | null {
        for (const value of Object.values(type)) {
            if (value instanceof type
                && value.id.toString() === id.toString()) {
                return value;
            }
        }
        return null;
    }

    public static getValue<T extends BaseEnum>(type: new() => T, id: string): T {
        const value = this.findValue(type, id);
        if (value) {
            return value;
        }
        throw Error('id :' + id + ' does not exist in ' + type);
    }
}
