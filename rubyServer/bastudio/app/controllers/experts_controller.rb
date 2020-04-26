class ExpertsController < ApplicationController
  def index
    @experts = Expert.all
    respond_to  do |format|
      format.html {render 'index'}
      format.json {render plain: @experts.to_json}
    end
  end

  def show
    @expert = Expert.find(params[:id])
  end

  def new
    @expert = Expert.new
    @procedures = Procedure.all
  end

  def edit
    @expert = Expert.find(params[:id])
    @procedures = Procedure.all
  end

  def create
    @expert = Expert.new(expert_params)

    @expert.save

    uploaded_io = params[:expert][:picture]
    File.open(Rails.root.join(
        ApplicationController::FILES_ROOT_FOLDER,
        ApplicationController::PICTURE_FOLDER + "/experts",
        @expert[:id].to_s + ApplicationController::DEFAULT_PICTURE_FORMAT), 'wb') do |file|
      file.write(uploaded_io.read)
    end

    extractExpProcedure

    if @expert.save
      redirect_to @expert
    else
      render 'new'
    end
  end

  def update
    @expert = Expert.find(params[:id])
    ExpProcedure.where(:expert_id => @expert.id).delete_all

    extractExpProcedure

    if @expert.update(expert_params)
      redirect_to @expert
    else
      render 'edit'
    end
  end

  def destroy
    @expert = Expert.find(params[:id])
    @expert.destroy

    redirect_to experts_path
  end


  def get_by_phone
    respond_to  do |format|
        format.json { render :json =>
          Expert.where(:phoneNumber => params[:phone_number])
          .first
          .time_slots
          .select { |timeSlot| timeSlot.date == Date.today }
          .uniq
          .to_json(:only => [:startTime, :endTime])
        }
    end
  end

  def get_possible_time_slots
    expertId = params[:expert_id]
    procedures = params[:procedures]
    expProcedures = ExpProcedure.where(:expert_id => expertId, :procedure_id => procedures).all
    totalTime = 0
    expProcedures.each do |expProcedure|
      totalTime += expProcedure.time_consume.to_i
   end
   possibleTime = []
   date = Date.today
   render plain: date.minutes
   # date.hour = 0
   # date.minute = 0
   # date.second = 0
   # render plain: date

  end

  private
    def expert_params
      params.require(:expert).permit(:firstName, :lastName,
        :lastName, :secondName, :description, :phoneNumber)
    end

    def procedure_params
      params.require(:procedure)
    end

    def extractExpProcedure
      @expProcedure = {}
      @expProcedure[:expert_id] = @expert[:id]
      procedure_params.each do |procedure|
        if procedure[1][:price].present? then
          @expProcedure[:procedure_id] = procedure[1][:id]
          @expProcedure[:price] = procedure[1][:price]
          @expProcedure[:time_consume] = procedure[1][:time]
          ExpProcedure.new(@expProcedure).save
        end
      end
    end
end
