import { serialize, deserialize, deserializeArray, plainToClass } from 'class-transformer';

export class JsonSerializer {
    mapToObject<T>(r: any, type?: new () => T): T {
        return type ? this.deserializeToObject(r, type) : r;
    }

    mapToArray<T>(r: any, type?: new () => T): T {
        return type ? this.deserializeToArray(r, type) : JSON.parse(r);
    }

    deserializeToObject<T>(json: any, type: new () => T): T {
        return deserialize(type, json);
    }

    deserializeToArray<T>(json: any, type: new () => T): T[] {
        return deserializeArray(type, json);
    }

    mapToJson<T>(object: T | T[], type?: new () => T): any {
        if (typeof object === 'string') {
            return object;
        }
        if (type) {
            const classObject = plainToClass(type, object);
            return serialize(classObject);
        } else {
            return object instanceof Array ? JSON.stringify(object) : object;
        }
    }
}
