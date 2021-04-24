import BaseEnum from './BaseEnum';

export default class SystemType extends BaseEnum {
    public static SOURCE = new SystemType('1', 'system:systemTypeEnum:source');
    public static TARGET = new SystemType('2', 'system:systemTypeEnum:target');
    public static UTILITY_SYSTEM = new SystemType('3', 'system:systemTypeEnum:utility');

    public constructor(id?: string, name?: string) {
        super(id, name);
    }
}
