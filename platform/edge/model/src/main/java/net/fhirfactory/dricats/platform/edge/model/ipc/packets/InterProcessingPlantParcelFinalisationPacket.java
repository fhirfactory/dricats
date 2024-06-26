package net.fhirfactory.dricats.platform.edge.model.ipc.packets;

import net.fhirfactory.dricats.core.model.generalid.FDNToken;
import net.fhirfactory.dricats.core.model.resilience.parcel.ResilienceParcelProcessingStatusEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class InterProcessingPlantParcelFinalisationPacket implements Serializable {
    private FDNToken episodeIdentifier;
    private FDNToken successorEpisodeIdentifier;
    private ResilienceParcelProcessingStatusEnum outcomeStatus;
    private String finalisationStatusPacketIdentifier;
    private LocalDateTime sendDate;

    public FDNToken getEpisodeIdentifier() {
        return episodeIdentifier;
    }

    public void setEpisodeIdentifier(FDNToken episodeIdentifier) {
        this.episodeIdentifier = episodeIdentifier;
    }

    public FDNToken getSuccessorEpisodeIdentifier() {
        return successorEpisodeIdentifier;
    }

    public void setSuccessorEpisodeIdentifier(FDNToken successorEpisodeIdentifier) {
        this.successorEpisodeIdentifier = successorEpisodeIdentifier;
    }

    public ResilienceParcelProcessingStatusEnum getOutcomeStatus() {
        return outcomeStatus;
    }

    public void setOutcomeStatus(ResilienceParcelProcessingStatusEnum outcomeStatus) {
        this.outcomeStatus = outcomeStatus;
    }

    public String getFinalisationStatusPacketIdentifier() {
        return finalisationStatusPacketIdentifier;
    }

    public void setFinalisationStatusPacketIdentifier(String finalisationStatusPacketIdentifier) {
        this.finalisationStatusPacketIdentifier = finalisationStatusPacketIdentifier;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }
}
