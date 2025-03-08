import * as fs from "node:fs";
import {env} from "$env/dynamic/private";
import {logger} from "$lib/server/logger";
import { Dashbaord} from "$lib/types";
import {building} from "$app/environment";

let dashboard: Dashbaord = new Dashbaord();

function loadItems(): void {
    const itemsText = fs.readFileSync(env.ITEMS_JSON_PATH || "", 'utf8')
    dashboard = JSON.parse(itemsText);
    logger.info(`dashboard reloaded with ${dashboard.items.length} items`);
}

if (!building) {
    if (!env.ITEMS_JSON_PATH) {
        logger.error("ITEMS_JSON_PATH not set");
    }

    fs.watchFile(env.ITEMS_JSON_PATH || "", () => {
        loadItems();
    })
    loadItems();
}

export {
    dashboard
};