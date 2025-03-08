export class Dashbaord {
    items: Item[] = [];

    constructor(init?: Partial<Dashbaord>) {
        Object.assign(this, init);
    }
}

export class Item {
    name: string;
    url: string;
    group: string | undefined;
    precedence: number = 0;
    iconUrl: string | undefined;
    healthUrl: string | undefined;

    constructor(name: string, url: string, init?: Partial<Omit<Item, "name" | "url">>) {
        this.name = name;
        this.url = url;
        Object.assign(this, init);
    }
}
