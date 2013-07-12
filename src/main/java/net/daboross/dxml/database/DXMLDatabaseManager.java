/*
 * Copyright (C) 2013 Dabo Ross <http://www.daboross.net/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daboross.dxml.database;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.daboross.dxml.DXMLException;
import net.daboross.dxml.DXMLHelper;
import org.w3c.dom.Node;

/**
 *
 * @author daboross
 */
public class DXMLDatabaseManager {

    private final DXMLDatabase mainDatabase;
    private final Map<String, DXMLDatabase> dataBaseMap = new HashMap<String, DXMLDatabase>();

    public DXMLDatabaseManager() throws DXMLException {
        mainDatabase = new DXMLDatabase();

    }

    /**
     * This gets the data under the given name, or creates a new one if it
     * doesn't exist.
     */
    public DXMLDatabase getDatabase(String name) {
        if (dataBaseMap.containsKey(name)) {
            return dataBaseMap.get(name);
        } else {
            DXMLDatabase newDatabase = new DXMLDatabase(mainDatabase.document, mainDatabase.newNode(name));
            dataBaseMap.put(name, newDatabase);
            return newDatabase;
        }
    }

    public DXMLDatabaseManager(File file) throws DXMLException {
        mainDatabase = new DXMLDatabase(file);
        List<String> keyList = mainDatabase.getAllNodeKeys();
        for (String str : keyList) {
            Node n;
            try {
                n = mainDatabase.getNode(str);
            } catch (WrongTypeException wte) {
                Logger.getLogger(DXMLDatabaseManager.class.getName()).log(Level.SEVERE, "Exception While getting value from key in DXMLDatabase.getAllNodeKeys()", wte);
                continue;
            } catch (EntryNotFoundException enfe) {
                Logger.getLogger(DXMLDatabaseManager.class.getName()).log(Level.SEVERE, "Exception While getting value from key in DXMLDatabase.getAllNodeKeys()", enfe);
                continue;
            }
            DXMLDatabase dxmld = new DXMLDatabase(mainDatabase.document, n);
            dataBaseMap.put(str, dxmld);
        }
    }

    public void saveToFile(File file) throws DXMLException {
        mainDatabase.pushValuesToDocument();
        DXMLHelper.writeXML(mainDatabase.document, file);
    }
}
