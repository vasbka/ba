class TimeSlotsController < ApplicationController
  def new
    @experts = Expert.all
    @expertsProcedures = ExpProcedure.all
  end

  def index
    # @result = {}
    # Expert.all.each {
    #   |expert|
    #   @result[expert.id] = {}
    #   @result[expert.id]['lastName'] = expert.lastName
    #   @result[expert.id]['firstName'] = expert.firstName
    #   @result[expert.id]['secondName'] = expert.secondName
    #   @result[expert.id]['slots'] = {}
    #   @expProcedureIds = ExpProcedure.where(:expert_id => expert.id).map { |expProcedure| expProcedure.id }
    #   TimeSlot.all.each {
    #     |timeSlot|
    #     if ((@expProcedureIds & timeSlot.exp_procedures).empty?)
    #
    #       # @result[expert.id]['slots'][timeSlot.id] = {}
    #       # ExpProcedure.where(:id => timeSlot.exp_procedures).all.each {
    #       #   |exp_procedure|
    #       #   @result[expert.id]['slots'][timeSlot.id]['procedures'] = {}
    #       #   @result[expert.id]['slots'][timeSlot.id]['procedures'][expProcedure.id]['procedureName'] = Procedure.where(:id => expProcedure.procedure_id).first.title
    #       #   @result[expert.id]['slots'][timeSlot.id]['procedures'][expProcedure.id]['price'] = timeSlot.price
    #       #   @result[expert.id]['slots'][timeSlot.id]['procedures'][expProcedure.id]['date'] = timeSlot.date
    #       #   @result[expert.id]['slots'][timeSlot.id]['procedures'][expProcedure.id]['startTime'] = timeSlot.startTime
    #       #   @result[expert.id]['slots'][timeSlot.id]['procedures'][expProcedure.id]['endTime'] = timeSlot.endTime
    #       # }
    #     end
    #   }
    # }
    # render plain: @expProcedureIds
    # @procedures = Procedure.all
    # @expProcedures = ExpProcedure.al
    @experts = Expert.all
    @timeSlots = TimeSlot.all
    # render plain: @timeSlots.to_json
    # render plain: @result.to_json
  end
end
