import BaseEnum from './BaseEnum';

export default class DataObjectLocationType extends BaseEnum {
    public static MY_COMPUTER = new DataObjectLocationType('1', 'dataObject:dataObjectLocationTypeEnum:myComputer');
    public static DCA_SERVER = new DataObjectLocationType('2', 'dataObject:dataObjectLocationTypeEnum:dcaServer');
    public static FTP = new DataObjectLocationType('3', 'dataObject:dataObjectLocationTypeEnum:ftp');

    public constructor(id?: string, name?: string) {
        super(id, name);
    }
}