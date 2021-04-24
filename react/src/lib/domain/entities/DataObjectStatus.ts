import BaseEnum from './BaseEnum';

export default class DataObjectStatus extends BaseEnum {
    public static SUCCESS = new DataObjectStatus('1', 'dataObject:dataObjectStatusEnum:success');
    public static WARNING = new DataObjectStatus('2', 'dataObject:dataObjectStatusEnum:warning');
    public static ERROR = new DataObjectStatus('3', 'dataObject:dataObjectStatusEnum:error');

    public constructor(id?: string, name?: string) {
        super(id, name);
    }
}