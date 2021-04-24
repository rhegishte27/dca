import BaseEnum from './BaseEnum';

export default class DataObjectType extends BaseEnum {
    public static COBOL_COPYBOOK = new DataObjectType('1', 'dataObject:dataObjectTypeEnum:cobolCopybook');
    public static JAVA_CLASS = new DataObjectType('2', 'dataObject:dataObjectTypeEnum:javaClass');
    public static DDL = new DataObjectType('3', 'dataObject:dataObjectTypeEnum:ddl');
    public static XML = new DataObjectType('4', 'dataObject:dataObjectTypeEnum:xml');
    public static MAINFRAME_ASSEMBLER = new DataObjectType('5', 'dataObject:dataObjectTypeEnum:mainframeAssembler');

    public constructor(id?: string, name?: string) {
        super(id, name);
    }
}