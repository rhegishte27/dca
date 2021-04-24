import BaseEnum from './BaseEnum';

export default class LocationType extends BaseEnum {
    public static NETWORK = new LocationType('1', 'location:locationTypeEnum:network');
    public static FTP = new LocationType('2', 'location:locationTypeEnum:ftp');

    public constructor(id?: string, name?: string) {
        super(id, name);
    }
}
