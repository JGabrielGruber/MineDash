--[=====[
  This is a sample applcation for OpenComputers, where, it'll monitor chests, and inform it's contents to the API

  The requirements are:
    - Computer
    - Internet Card
    - Adapter
    - Inventory Controller Module
   
   The Adapter should be placed bellow the chest

]=====]--


local component = require("component")
local internet = require("internet")
local event = require("event")

function collectLocalInfo()
    for i,name in pairs(component.list()) do
        local displayNames = {};
        local items = {};
        local p = component.proxy(i);
        if (p.getInventorySize ~= nil) then
            if (string.find(tostring(p.getInventorySize),"side:number") ~= nil) then
                for j=0,5,1 do
                    local chestSize = nil;
                    chestSize = p.getInventorySize(j);
                    if (chestSize ~= nil) then
                        for k=1,chestSize,1 do
                            local iteminfo = nil;
                            if (string.find(tostring(p.getStackInSlot),"side:number") ~= nil) then
                                iteminfo = p.getStackInSlot(j,k);
                            else
                                iteminfo = p.getStackInSlot(k);
                            end
                            if (iteminfo) then
                                displayname = iteminfo.label;
                                if (displayname == nil) then
                                    displayname = iteminfo.display_name;
                                end
                                fingerlog = iteminfo.name;
                                local amount = iteminfo.size;
                                if (amount == nil) then amount = 0; end
                                sendUpdate(displayname,amount);
                            end
                        end
                    end
                end
            else
                chestSize = p.getInventorySize();
                if (chestSize ~= nil) then
                    for k=1,chestSize,1 do
                        local iteminfo = nil;
                        iteminfo = p.getStackInSlot(k);
                        if (iteminfo) then
                            displayname = iteminfo.label;
                            if (displayname == nil) then
                                displayname = iteminfo.display_name;
                            end
                            fingerlog = iteminfo.name;
                            local amount = iteminfo.size;
                            if (amount == nil) then amount = 0; end
                            sendUpdate(displayname,amount);
                        end
                    end
                end
            end
        end
    end
end

function sendUpdate(displayname, amount)
    local name = displayname:lower()
    local spc = name:find(" ")
    if (spc ~= nill and spc > 0) then
        name = name:sub(0,spc-1)
    end
    local handle = internet.request("http://127.0.0.1:8080/ores/" .. name .. "/qtd", {amount=amount}, {}, "POST")
end

-- sendUpdate("Iron Ore", 10)
-- collectLocalInfo()

while true do
  collectLocalInfo()
  os.sleep(1)
  if event.pull(0.05, "interrupted") then
    os.exit()
  end
end
