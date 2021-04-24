import BaseEntity from './BaseEntity';
import LocationType from './LocationType';
import PlatformType from './PlatformType';

export default class Location extends BaseEntity {
    public id: string = '';
    public identifier: string = '';
    public locationType: LocationType = new LocationType();
    public path: string = '';
    public serverName: string | undefined;
    public userName: string | undefined;
    public password: string | undefined;
    public platformType: PlatformType | undefined;
}
