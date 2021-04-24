import BaseEnum from './BaseEnum';

export default class TypeProjectElement extends BaseEnum {
    public static SOURCE_CODE = new TypeProjectElement('1', 'project:projectElementsEnum:generatedCode');
    public static MAPS = new TypeProjectElement('2', 'project:projectElementsEnum:maps');
    public static TABLE_DATA_DICTIONARY = new TypeProjectElement('3', 'project:projectElementsEnum:tableDD');
    public static BACKUPS = new TypeProjectElement('4', 'project:projectElementsEnum:backups');

    public constructor(id?: string, name?: string) {
        super(id, name);
    }
}