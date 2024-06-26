/*
 * Copyright (c) 2021 Mark A. Hunter
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.fhirfactory.dricats.itops.im.workshops.datagrid.topologymaps;

import net.fhirfactory.dricats.itops.im.workshops.transform.matrixbridge.common.ParticipantRoomIdentityFactory;
import net.fhirfactory.pegacorn.communicate.matrix.model.core.MatrixRoom;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ITOpsKnownRoomAndSpaceMapDM {
    private static final Logger LOG = LoggerFactory.getLogger(ITOpsKnownRoomAndSpaceMapDM.class);

    // ConcurrentHashMap<roomId, MatrixRoom>
    private ConcurrentHashMap<String, MatrixRoom> knownRooms;
    private Object knownRoomsLock;

    // ConcurrentHashMap<pseudoAlias, roomId> (not the Canonical Alias Id, the Alias which is in the Id)
    private ConcurrentHashMap<String, String> pseudoAliasRoomMap;
    private Object knownPseudoAliasRoomMapLock;

    // ConcurrentHashMap<roomAlias, roomId> (not the Canonical Alias Id, the Alias which is in the Id)
    private ConcurrentHashMap<String, String> canonicalAliasRoomMap;
    private Object knownCanonicalAliasRoomMapLock;

    // ConcurrentHashMap<roomId, SynapseRoom>
    private ConcurrentHashMap<String, MatrixRoom> lastUsedRoomSet;
    private Object knownLastScannedRoomSet;

    @Inject
    private ParticipantRoomIdentityFactory roomIdentityFactory;

    //
    // Constructor(s)
    //

    public ITOpsKnownRoomAndSpaceMapDM() {
        this.knownRooms = new ConcurrentHashMap<>();
        this.pseudoAliasRoomMap = new ConcurrentHashMap<>();
        this.canonicalAliasRoomMap = new ConcurrentHashMap<>();
        this.lastUsedRoomSet = new ConcurrentHashMap<>();
        this.knownRoomsLock = new Object();
        this.knownPseudoAliasRoomMapLock = new Object();
        this.knownCanonicalAliasRoomMapLock = new Object();
        this.knownLastScannedRoomSet = new Object();
    }

    //
    // Getters (and Setters)
    //

    public ConcurrentHashMap<String, MatrixRoom> getLastUsedRoomSet() {
        return lastUsedRoomSet;
    }

    protected ConcurrentHashMap<String, MatrixRoom> getKnownRooms() {
        return knownRooms;
    }

    public ConcurrentHashMap<String, String> getPseudoAliasRoomMap() {
        return pseudoAliasRoomMap;
    }

    public ConcurrentHashMap<String, String> getCanonicalAliasRoomMap() {
        return canonicalAliasRoomMap;
    }

    protected Logger getLogger() {
        return (LOG);
    }

    protected Object getKnownRoomsLock() {
        return knownRoomsLock;
    }

    protected Object getKnownPseudoAliasRoomMapLock() {
        return knownPseudoAliasRoomMapLock;
    }

    protected Object getKnownCanonicalAliasRoomMapLock() {
        return knownCanonicalAliasRoomMapLock;
    }

    protected Object getKnownLastScannedRoomSet() {
        return knownLastScannedRoomSet;
    }

    protected ParticipantRoomIdentityFactory getRoomIdentityFactory() {
        return roomIdentityFactory;
    }

    //
    // Business Methods
    //

    public void addRoom(MatrixRoom room) {
        getLogger().debug(".addRoomFromMatrix(): Entry");
        getLogger().trace(".addRoomFromMatrix(): room->{}", room);
        if (room == null) {
            getLogger().debug(".addRoomFromMatrix(): Exit, synapseRoom is null");
            return;
        }

        boolean storeAsSpace = false;
        boolean storeAsRoom = false;

        String alias = room.getCanonicalAlias();
        String roomId = room.getRoomID();

        if (StringUtils.isNotEmpty(roomId)) {
            synchronized (getKnownRoomsLock()) {
                getLogger().debug("addRoomFromMatrix(): Adding synapseRoom to roomId/knownRooms map");
                if (getKnownRooms().containsKey(roomId)) {
                    getKnownRooms().remove(roomId);
                }
                getKnownRooms().put(roomId, room);
            }
        }

        getLogger().debug("addRoomFromMatrix(): Adding synapseRoom to canonicalAlias/knownRooms Map");
        if (StringUtils.isNotEmpty(alias) && StringUtils.isNotEmpty(roomId)) {
            synchronized (getCanonicalAliasRoomMap()) {
                if (getCanonicalAliasRoomMap().containsKey(alias)) {
                    getCanonicalAliasRoomMap().remove(alias);
                }
                getCanonicalAliasRoomMap().put(alias, roomId);
            }
        }

        getLogger().debug("addRoomFromMatrix(): Adding synapseRoom to pseudoAlias/knownRooms Map");
        if (StringUtils.isNotEmpty(alias) && StringUtils.isNotEmpty(roomId)) {
            String pseudoAliasFromAliasId = getPseudoAliasFromAliasId(alias);
            if (StringUtils.isNotEmpty(pseudoAliasFromAliasId)) {
                synchronized (getKnownPseudoAliasRoomMapLock()) {
                    if (getPseudoAliasRoomMap().containsKey(pseudoAliasFromAliasId)) {
                        getPseudoAliasRoomMap().remove(pseudoAliasFromAliasId);
                    }
                    getPseudoAliasRoomMap().put(pseudoAliasFromAliasId, roomId);
                }
            }
        }

        getLogger().debug("addRoomFromMatrix(): Exit");
    }

    public MatrixRoom getRoomFromRoomId(String roomId) {
        getLogger().debug(".getRoomFromRoomId(): Entry, roomId->{}", roomId);
        MatrixRoom room = null;
        if (StringUtils.isNotEmpty(roomId)) {
            synchronized (getKnownRoomsLock()) {
                if (getKnownRooms().containsKey(roomId)) {
                    room = getKnownRooms().get(roomId);
                }
            }
        }
        getLogger().debug(".getRoomFromRoomId(): Exit, room->{}", room);
        return (room);
    }

    public void deleteRoom(String roomId) {
        getLogger().debug(".deleteRoom(): Entry, roomId->{}", roomId);
        boolean roomIsDeleted = false;
        boolean roomITOpsAliasIsDeleted = false;
        boolean roomCanonicalAliasIsDeleted = false;
        if (StringUtils.isNotEmpty(roomId)) {
            synchronized (getKnownRoomsLock()) {
                if (getKnownRooms().containsKey(roomId)) {
                    getKnownRooms().remove(roomId);
                    roomIsDeleted = true;
                }
            }
            synchronized (getKnownCanonicalAliasRoomMapLock()) {
                Collection<String> values = getCanonicalAliasRoomMap().values();
                if (values.contains(roomId)) {
                    Enumeration<String> keys = getCanonicalAliasRoomMap().keys();
                    while (keys.hasMoreElements()) {
                        String currentCanonicalAlias = keys.nextElement();
                        String currentRoomId = getCanonicalAliasRoomMap().get(currentCanonicalAlias);
                        if (currentRoomId.contentEquals(roomId)) {
                            getCanonicalAliasRoomMap().remove(currentCanonicalAlias);
                            roomCanonicalAliasIsDeleted = true;
                            break;
                        }
                    }
                }
            }
            synchronized (getKnownPseudoAliasRoomMapLock()) {
                Collection<String> values = getPseudoAliasRoomMap().values();
                if (values.contains(roomId)) {
                    Enumeration<String> keys = getPseudoAliasRoomMap().keys();
                    while (keys.hasMoreElements()) {
                        String currentPseudoAlias = keys.nextElement();
                        String currentRoomId = getPseudoAliasRoomMap().get(currentPseudoAlias);
                        if (currentRoomId.contentEquals(roomId)) {
                            getPseudoAliasRoomMap().remove(currentPseudoAlias);
                            roomITOpsAliasIsDeleted = true;
                            break;
                        }
                    }
                }
            }
        }
        getLogger().debug(".deleteRoom(): Exit, roomIsDeleted->{}, roomITOpsAliasIsDeleted->{}, roomCanonicalAliasIsDeleted->{}", roomIsDeleted, roomITOpsAliasIsDeleted, roomCanonicalAliasIsDeleted);
    }

    public MatrixRoom getRoomFromCanonicalAlias(String canonicalAlias) {
        getLogger().debug(".getRoomFromCanonicalAlias(): Entry, canonicalAlias->{}", canonicalAlias);
        MatrixRoom room = null;
        String roomId = null;
        if (StringUtils.isNotEmpty(canonicalAlias)) {
            synchronized (getKnownCanonicalAliasRoomMapLock()) {
                if (getCanonicalAliasRoomMap().containsKey(canonicalAlias)) {
                    roomId = getCanonicalAliasRoomMap().get(canonicalAlias);
                }
            }
        }
        if (StringUtils.isNotEmpty(roomId)) {
            synchronized (getKnownRoomsLock()) {
                if (getKnownRooms().containsKey(roomId)) {
                    room = getKnownRooms().get(roomId);
                }
            }
        }
        getLogger().debug(".getRoomFromCanonicalAlias(): Exit, room->{}", room);
        return (room);
    }

    public MatrixRoom getRoomFromPseudoAlias(String pseudoAlias) {
        getLogger().debug(".getRoomFromPseudoAlias(): Entry, pseudoAlias->{}", pseudoAlias);
        MatrixRoom room = null;
        String roomId = null;
        if (StringUtils.isNotEmpty(pseudoAlias)) {
            synchronized (getKnownPseudoAliasRoomMapLock()) {
                if (getPseudoAliasRoomMap().containsKey(pseudoAlias)) {
                    roomId = getPseudoAliasRoomMap().get(pseudoAlias);
                }
            }
        }
        if (StringUtils.isNotEmpty(roomId)) {
            synchronized (getKnownRoomsLock()) {
                if (getKnownRooms().containsKey(roomId)) {
                    room = getKnownRooms().get(roomId);
                }
            }
        }
        getLogger().debug(".getRoomFromPseudoAlias(): Exit, room->{}", room);
        return (room);
    }

    public String getRoomIdFromPseudoAlias(String alias) {
        getLogger().debug(".getRoomIdFromPseudoAlias(): Entry, alias->{}", alias);

        if (StringUtils.isEmpty(alias)) {
            getLogger().debug(".getRoomIdFromPseudoAlias(): Exit, alias is empty");
            return (null);
        }

        String roomId = null;
        synchronized (getKnownPseudoAliasRoomMapLock()) {
            if (getPseudoAliasRoomMap().containsKey(alias)) {
                roomId = getPseudoAliasRoomMap().get(alias);
            }
        }

        getLogger().debug(".getRoomIdFromAlias(): Exit, roomId->{}", roomId);
        return (roomId);
    }

    public String getRoomIdFromCanonicalAlias(String canonicalAlias) {
        getLogger().debug(".getRoomIdFromCanonicalAlias(): Entry, canonicalAlias->{}", canonicalAlias);

        if (StringUtils.isEmpty(canonicalAlias)) {
            getLogger().debug(".getRoomIdFromCanonicalAlias(): Exit, alias is empty");
            return (null);
        }

        String roomId = null;
        synchronized (getKnownPseudoAliasRoomMapLock()) {
            if (getPseudoAliasRoomMap().containsKey(canonicalAlias)) {
                roomId = getPseudoAliasRoomMap().get(canonicalAlias);
            }
        }

        getLogger().debug(".getRoomIdFromCanonicalAlias(): Exit, roomId->{}", roomId);
        return (roomId);
    }

    private String getPseudoAliasFromAliasId(String aliasId) {
        getLogger().debug(".getPseudoAliasFromAliasId(): Entry, aliasId->{}", aliasId);
        if (StringUtils.isEmpty(aliasId)) {
            return (null);
        }
        String clonedAliasId = SerializationUtils.clone(aliasId);
        String aliasIdWithoutFirstChar = clonedAliasId.substring(1);
        String[] aliasSplit = aliasIdWithoutFirstChar.split(":");
        String alias = null;
        if (aliasSplit.length > 0) {
            alias = aliasSplit[0];
        }
        getLogger().debug(".getPseudoAliasFromAliasId(): Exit, alias->{}", alias);
        return (alias);
    }

    public Set<MatrixRoom> getFullRoomSet() {
        getLogger().debug(".getFullRoomSet(): Entry");
        Set<MatrixRoom> roomSet = new HashSet<>();
        if (getKnownRooms().isEmpty()) {
            return (roomSet);
        }
        synchronized (getKnownRoomsLock()) {
            roomSet.addAll(getKnownRooms().values());
        }
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(".getFullRoomSet(): Exit, roomSet.size->{}", roomSet.size());
        }
        getLogger().trace(".getFullRoomSet(): Exit, roomSet->{}", roomSet);
        return (roomSet);
    }

    public Set<MatrixRoom> getRecentlyAddedRooms() {
        getLogger().debug(".getRecentlyAddedRooms(): Entry");
        Set<MatrixRoom> addedRoomSet = new HashSet<>();
        if (getKnownRooms().isEmpty()) {
            return (addedRoomSet);
        }
        synchronized (getKnownRoomsLock()) {
            Collection<MatrixRoom> currentKnownRoomSet = getKnownRooms().values();
            if (getLastUsedRoomSet().isEmpty()) {
                addedRoomSet.addAll(currentKnownRoomSet);
            } else {
                Collection<MatrixRoom> previousKnownRoomSet = getLastUsedRoomSet().values();
                for (MatrixRoom currentKnownRoom : currentKnownRoomSet) {
                    boolean currentKnownRoomWasThere = false;
                    for (MatrixRoom previousKnownRoom : previousKnownRoomSet) {
                        if (currentKnownRoom.getRoomID().contentEquals(previousKnownRoom.getRoomID())) {
                            currentKnownRoomWasThere = true;
                            break;
                        }
                    }
                    if (!currentKnownRoomWasThere) {
                        addedRoomSet.add(currentKnownRoom);
                    }
                }
                getLastUsedRoomSet().clear();
            }
            for (MatrixRoom currentKnownRoom : currentKnownRoomSet) {
                getLastUsedRoomSet().put(currentKnownRoom.getRoomID(), currentKnownRoom);
            }
        }
        getLogger().debug(".getRecentlyAddedRooms(): Exit, addedRoomSet->{}", addedRoomSet);
        return (addedRoomSet);
    }

}
